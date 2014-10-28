/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.impl;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Message;
import de.dfki.kiara.MessageConnection;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportMessageListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public abstract class AbstractMessageConnection implements MessageConnection, TransportMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageConnection.class);
    private final TransportConnection transportConnection;
    private final IdentityHashMap<Message, TransportMessage> messageMap;
    private final List<Dispatcher> dispatchers;

    private static class Dispatcher extends AbstractFuture<Message> {

        private final Object messageId;

        Dispatcher(Object messageId) {
            this.messageId = messageId;
        }

        public boolean process(Message message) throws Exception {
            final Protocol protocol = message.getProtocol();

            if ((message.getMessageKind() == Message.Kind.RESPONSE
                    || message.getMessageKind() == Message.Kind.EXCEPTION)
                    && protocol.equalMessageIds(messageId, message.getMessageId())) {
                set(message);
                return true;
            }
            return false;
        }

    }

    AbstractMessageConnection(TransportConnection transportConnection) {
        this.transportConnection = transportConnection;
        this.transportConnection.addMessageListener(this);
        this.messageMap = new IdentityHashMap<>();
        this.dispatchers = new ArrayList<>();
    }

    @Override
    public ListenableFuture<Void> send(Message message) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        try {
            final TransportMessage tmessage;

            synchronized (messageMap) {
                tmessage = messageMap.remove(message.getRequestMessage());
            }

            final TransportMessage tresponse = transportConnection.createTransportMessage(tmessage);
            tresponse.setPayload(message.getMessageData());
            tresponse.setContentType(message.getProtocol().getMimeType());
            return transportConnection.send(tresponse);
        } catch (Exception ex) {
            logger.error("Could not send message", ex);
            return Futures.immediateFailedFuture(ex);
        }
    }

    @Override
    public ListenableFuture<Message> receive(Object messageId) {
        final Dispatcher dispatcher = new Dispatcher(messageId);
        synchronized (dispatchers) {
            dispatchers.add(dispatcher);
        }
        return dispatcher;
    }

    @Override
    public void close() throws IOException {
        transportConnection.close();
    }

    public TransportConnection getTransportConnection() {
        return transportConnection;
    }

    protected void processMessage(Message message, InvocationEnvironment env, ServiceMethodBinding serviceMethodBinding) {
        // FIXME compare with TransportMessageConnection
        assert message != null;

        try {
            logger.info("Incoming message: {}", message);

            switch (message.getMessageKind()) {
                case RESPONSE:
                case EXCEPTION:

                    // process via pipeline
                    boolean processed = false;
                    synchronized (dispatchers) {
                        for (Dispatcher handler : dispatchers) {
                            if (handler.process(message)) {
                                dispatchers.remove(handler);
                                processed = true;
                                break;
                            }
                        }
                    }
                    if (!processed) {
                        logger.warn("Unprocessed transport message: {}: {}", message.getClass(), message);
                    }
                    break;
                case REQUEST:

                    ListenableFuture<Message> fmsg = serviceMethodBinding.performLocalCall(env, message);

                    Futures.addCallback(fmsg, new FutureCallback<Message>() {

                        @Override
                        public void onSuccess(Message resultMessage) {
                            try {
                                send(resultMessage);
                            } catch (Exception ex) {
                                logger.error("Error on callback response", ex);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            logger.error("Could not process message", t);
                        }
                    }, Global.executor);

                    break;
                default:
                    logger.warn("Unprocessed message: {}: {}", message.getClass(), message);
                    break;
            }
        } catch (Exception ex) {
            logger.error("Message processing failed", ex);
        }
    }

    protected void processMessage(Protocol protocol, InvocationEnvironment env, ServiceMethodBinding serviceMethodBinding, TransportMessage trequest) throws Exception {
        final Message message = protocol.createMessageFromData(trequest.getPayload());

        synchronized (messageMap) {
            messageMap.put(message, trequest);
        }
        processMessage(message, env, serviceMethodBinding);
    }

}

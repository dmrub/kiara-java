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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Message;
import de.dfki.kiara.MessageConnection;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportMessageListener;
import de.dfki.kiara.util.Pipeline;
import java.io.IOException;
import java.util.IdentityHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public abstract class AbstractMessageConnection implements MessageConnection, TransportMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageConnection.class);
    private final TransportConnection transportConnection;
    protected final IdentityHashMap<Message, TransportMessage> messageMap;
    protected final Pipeline pipeline;

    AbstractMessageConnection(TransportConnection transportConnection) {
        this.transportConnection = transportConnection;
        this.transportConnection.addMessageListener(this);
        this.messageMap = new IdentityHashMap<>();
        this.pipeline = new Pipeline();
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
        final MessageDispatcher dispatcher = new MessageDispatcher(messageId);
        pipeline.addHandler(dispatcher);
        Futures.addCallback(dispatcher, new FutureCallback<Message>() {

            @Override
            public void onSuccess(Message result) {
                pipeline.removeHandler(dispatcher);
            }

            @Override
            public void onFailure(Throwable t) {
                pipeline.removeHandler(dispatcher);
            }
        });
        return dispatcher;
    }

    @Override
    public void close() throws IOException {
        transportConnection.close();
    }

    public TransportConnection getTransportConnection() {
        return transportConnection;
    }
}

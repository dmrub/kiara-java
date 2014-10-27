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
import de.dfki.kiara.Protocol;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportMessageConnection extends AbstractMessageConnection {

    private static final Logger logger = LoggerFactory.getLogger(TransportMessageConnection.class);
    private final Protocol protocol;
    private final ServiceMethodBinding serviceMethodBinding;

    public TransportMessageConnection(TransportConnection transportConnection, ServiceMethodBinding serviceMethodBinding, Protocol protocol) {
        super(transportConnection);
        this.protocol = protocol;
        this.serviceMethodBinding = serviceMethodBinding;
    }

    @Override
    public void onMessage(TransportMessage tmessage) {
        if (tmessage == null) {
            logger.error("Received null transport message");
            return;
        }

        try {
            Message message = protocol.createMessageFromData(tmessage.getPayload());

            synchronized (messageMap) {
                messageMap.put(message, tmessage);
            }

            processMessage(message);
        } catch (Exception ex) {
            logger.error("Message processing failed", ex);
        }
    }

    private void processMessage(Message message) {
        // FIXME compare with ServerConnectionHandler
        assert message != null;

        try {
            logger.info("Incoming message: {}", message);

            switch (message.getMessageKind()) {
                case RESPONSE:
                case EXCEPTION:
                    // process via pipeline
                    final Object processResult = pipeline.process(message);
                    if (processResult != null) {
                        logger.warn("Unprocessed transport message: {}: {}", processResult.getClass(), processResult);
                    }
                    break;
                case REQUEST:

                    ListenableFuture<Message> fmsg = serviceMethodBinding.performLocalCall(null, message);

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
                            logger.error("Error on callback response", t);
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

}

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

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Handler;
import de.dfki.kiara.Message;
import de.dfki.kiara.MessageConnection;
import de.dfki.kiara.MessageListener;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.RequestHandler;
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
public class TransportMessageConnection implements MessageConnection, TransportMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(TransportMessageConnection.class);
    private final TransportConnection transportConnection;
    private final Protocol protocol;
    private final List<MessageListener> listeners;
    private final IdentityHashMap<Message, TransportMessage> messageMap;

    public TransportMessageConnection(TransportConnection transportConnection, Protocol protocol) {
        this.transportConnection = transportConnection;
        this.transportConnection.addMessageListener(this);
        this.protocol = protocol;
        this.listeners = new ArrayList<>();
        this.messageMap = new IdentityHashMap<>();
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

            final TransportMessage tresponse = transportConnection.createResponse(tmessage);
            tresponse.setPayload(message.getMessageData());
            tresponse.setContentType(protocol.getMimeType());
            return transportConnection.send(tresponse);
        } catch (Exception ex) {
            logger.error("Error when sending message", ex);
        }
        return null;
    }

    @Override
    public void addMessageListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeMessageListener(MessageListener listener) {
        synchronized (listeners) {
            return listeners.remove(listener);
        }
    }

    @Override
    public void close() throws IOException {
        transportConnection.close();
    }

    private void processMessage(Message message) {
        synchronized (listeners) {
            for (MessageListener listener : listeners) {
                listener.onMessage(this, message);
            }
        }
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

}

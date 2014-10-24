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

import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.util.Pipeline;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class MessageDispatcher implements Pipeline.Handler {

    private final Object messageId;
    private final BlockingQueue<Object> messageQueue;

    MessageDispatcher(Object messageId) {
        this.messageId = messageId;
        this.messageQueue = new ArrayBlockingQueue<>(1);
    }

    public BlockingQueue<Object> getQueue() {
        return messageQueue;
    }

    @Override
    public Object process(Object input) throws Exception {
        if (!(input instanceof Message)) {
            return input;
        }

        final Message message = (Message) input;
        final Protocol protocol = message.getProtocol();

        if ((message.getMessageKind() == Message.Kind.RESPONSE
                || message.getMessageKind() == Message.Kind.EXCEPTION)
                && protocol.equalMessageIds(messageId, message.getMessageId())) {
            messageQueue.add(message);
            return null; // stop processing
        }
        return input;
    }

}
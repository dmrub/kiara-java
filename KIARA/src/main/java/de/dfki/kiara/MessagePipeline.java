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
package de.dfki.kiara;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class MessagePipeline {

    public static interface Handler {
        boolean process(Message input) throws Exception;
    }

    private final List<Handler> handlers;

    public MessagePipeline() {
        this.handlers = new ArrayList<>();
    }

    public void addHandler(Handler handler) {
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    public void removeHandler(Handler handler) {
        synchronized (handlers) {
            handlers.remove(handler);
        }
    }

    public boolean dispatchMessage(Message message) throws Exception {
        synchronized (handlers) {
            for (Handler handler : handlers) {
                if (handler.process(message)) {
                    handlers.remove(handler);
                    return true;
                }
            }
        }
        return false;
    }

    private static class Dispatcher extends AbstractFuture<Message> implements Handler {

        private final Object messageId;

        Dispatcher(Object messageId) {
            this.messageId = messageId;
        }

        @Override
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

    public ListenableFuture<Message> receiveMessage(Object messageId) {
        Dispatcher dispatcher = new Dispatcher(messageId);
        addHandler(dispatcher);
        return dispatcher;
    }

}

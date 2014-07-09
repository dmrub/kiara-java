/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;
import de.dfki.kiara.util.Pipeline;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcMessageDispatcher implements Pipeline.Handler {

    private final JsonRpcProtocol protocol;
    private final Object messageId;
    private final Method method;
    private final BlockingQueue<JsonRpcMessage> messageQueue;

    JsonRpcMessageDispatcher(JsonRpcProtocol protocol, Object messageId, Method method) {
        this.protocol = protocol;
        this.messageId = messageId;
        this.method = method;
        this.messageQueue = new ArrayBlockingQueue<>(1);
    }

    public BlockingQueue<JsonRpcMessage> getQueue() {
        return messageQueue;
    }

    @Override
    public Object process(Object input) throws Exception {
        if (!(input instanceof JsonNode)) {
            return input;
        }

        JsonNode node = (JsonNode) input;
        Object id = JsonRpcProtocol.parseMessageId(node);

        if (JsonRpcProtocol.equalIds(id, messageId)) {
            JsonRpcMessage message = protocol.createResponseMessageFromData(node, method);
            messageQueue.add(message);
            return null; // stop processing
        }
        return input;
    }

}

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
package de.dfki.kiara.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dfki.kiara.*;
import de.dfki.kiara.impl.DefaultInvocationHandler;
import de.dfki.kiara.impl.ServiceMethodBinding;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcProtocol implements Protocol {

    private final AtomicLong nextId;
    private final ObjectMapper objectMapper;
    private final ObjectReader objectReader;
    private final ObjectWriter objectWriter;

    public Object parseMessageName(JsonNode messageNode) throws IOException {
        JsonNode methodNode = messageNode.get("method");
        Object method = null;
        if (methodNode != null) {
            if (!methodNode.isTextual() && !methodNode.isNull()) {
                throw new IOException("Invalid 'id' member");
            }
            method = methodNode.textValue();
        }
        return method;
    }

    private static abstract class JsonRpcErrorMixIn {

        public JsonRpcErrorMixIn(@JsonProperty("code") int code, @JsonProperty("message") String message, @JsonProperty("data") Object data) {
        }
    }

    private static class JsonRpcModule extends SimpleModule {

        public JsonRpcModule() {
            super("JsonRpcModule",
                    new com.fasterxml.jackson.core.Version(1, 0, 0, null, null, null));
            addDeserializer(Void.TYPE, new JsonDeserializer<Void>() {

                @Override
                public Void deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                    if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
                        return null;
                    }
                    throw ctxt.wrongTokenException(jp, JsonToken.VALUE_NULL, "expected JSON null token");
                }

            });
            setMixInAnnotation(JsonRpcError.class, JsonRpcErrorMixIn.class);
        }

    }

    private static com.fasterxml.jackson.databind.Module createSerializationModule() {
        return new JsonRpcModule();
    }

    public JsonRpcProtocol() {
        nextId = new AtomicLong(1);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(createSerializationModule());
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        objectReader = objectMapper.reader();
        objectWriter = objectMapper.writer();
    }

    public long getNextId() {
        return nextId.getAndIncrement();
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public Message createMessageFromData(ByteBuffer data) throws IOException {
        return new JsonRpcMessage(this, data);
    }

    public static Object parseMessageId(JsonNode messageNode) throws MessageDeserializationException {
        JsonNode idNode = messageNode.get("id");
        Object id = null;
        if (idNode != null) {
            if (!idNode.isTextual() && !idNode.isNumber() && !idNode.isNull()) {
                throw new MessageDeserializationException("Invalid 'id' member");
            }
            if (idNode.isTextual()) {
                id = idNode.textValue();
            } else if (idNode.isNumber()) {
                id = idNode.numberValue();
            }
        }
        return id;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ObjectReader getObjectReader() {
        return objectReader;
    }

    public ObjectWriter getObjectWriter() {
        return objectWriter;
    }

    public JsonRpcMessage createResponseMessageFromData(JsonNode node) throws IOException {
        return new JsonRpcMessage(this, node);
    }

    @Override
    public boolean equalMessageIds(Object id1, Object id2) {
        return equalIds(id1, id2);
    }

    public static boolean equalIds(Object id1, Object id2) {
        if (id1 == id2) {
            return true;
        }
        if (id1 == null || id2 == null) {
            return false;
        }

        if (isIntegral(id1) && isIntegral(id2)) {
            if (((Number) id1).longValue() == ((Number) id2).longValue()) {
                return true;
            }
        }
        return id1.equals(id2);
    }

    private static boolean isIntegral(Object obj) {
        return obj instanceof Long || obj instanceof Integer || obj instanceof Short || obj instanceof Byte;
    }

    @Override
    public Message createRequestMessage(Message.RequestObject request) throws IOException {
        return new JsonRpcMessage(this, request, getNextId());
    }

    @Override
    public Message createResponseMessage(Message requestMessage, Message.ResponseObject response) {
        return new JsonRpcMessage(this, response, ((JsonRpcMessage)requestMessage));
    }

    @Override
    public InterfaceCodeGen createInterfaceCodeGen(final ConnectionBase connection) {
        final JsonRpcProtocol thisProtocol = this;
        return new InterfaceCodeGen() {
            @Override
            public <T> T generateInterfaceImpl(Class<T> interfaceClass, InterfaceMapping<T> mapping) {
                final ServiceMethodBinding smb = (ServiceMethodBinding)connection.getServiceMethodExecutor();
                Object impl = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                        new Class<?>[]{interfaceClass, RemoteInterface.class},
                        new DefaultInvocationHandler(connection, mapping, thisProtocol));
                return interfaceClass.cast(impl);
            }
        };
    }

}

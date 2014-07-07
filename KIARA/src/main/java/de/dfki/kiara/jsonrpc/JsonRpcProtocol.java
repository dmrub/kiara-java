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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dfki.kiara.Connection;
import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.InterfaceCodeGen;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.RemoteInterface;
import de.dfki.kiara.util.ByteBufferInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcProtocol implements Protocol, InterfaceCodeGen {

    private Connection connection;
    private final AtomicLong nextId;
    private final ObjectMapper objectMapper;

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
                public Void deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
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
        connection = null;
        nextId = new AtomicLong(1);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(createSerializationModule());
    }

    public long getNextId() {
        return nextId.getAndIncrement();
    }

    @Override
    public void initConnection(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can't be null");
        }
        if (this.connection != null) {
            throw new IllegalStateException("connection was already initialized");
        }
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    /**
     *
     * @param data
     * @param method
     * @return
     * @throws IOException
     */
    @Override
    public Message createRequestMessageFromData(ByteBuffer data, Method method) throws IOException {
        JsonNode node = objectMapper.readTree(new ByteBufferInputStream(data));

        JsonNode jsonrpcNode = node.get("jsonrpc");
        if (jsonrpcNode == null || !jsonrpcNode.isTextual()) {
            throw new IOException("Not a jsonrpc protocol");
        }

        if (!"2.0".equals(jsonrpcNode.textValue())) {
            throw new IOException("Not a jsonrpc 2.0");
        }

        JsonNode methodNode = node.get("method");
        if (methodNode == null) {
            throw new IOException("No 'method' member in the request object");
        }

        if (!methodNode.isTextual()) {
            throw new IOException("Member 'method' in request object is not a string");
        }

        JsonNode paramsNode = node.get("params");

        Object[] args = null;
        if (paramsNode != null) {
            if (!paramsNode.isArray() && !paramsNode.isObject()) {
                throw new IOException("Member 'params' is neither array nor object");
            }

            if (paramsNode.isArray()) {
                Class<?>[] paramTypes = method.getParameterTypes();

                if (paramTypes.length != paramsNode.size()) {
                    throw new IOException("Member 'params' size is: " + paramsNode.size() + ", required " + paramTypes.length);
                }

                args = new Object[paramTypes.length];

                for (int i = 0; i < paramsNode.size(); ++i) {
                    args[i] = objectMapper.treeToValue(paramsNode.get(i), paramTypes[i]);
                }
            } else {
                throw new UnsupportedOperationException("Object is not supported as 'params' member");
            }
        }

        JsonNode idNode = node.get("id");
        Object id = null;
        if (idNode != null) {
            if (!idNode.isTextual() && !idNode.isNumber() && !idNode.isNull()) {
                throw new IOException("Invalid 'id' member");
            }
            if (idNode.isTextual()) {
                id = idNode.textValue();
            } else if (idNode.isNumber()) {
                id = idNode.numberValue();
            }
        }

        return new JsonRpcMessage(this,
                new Message.RequestObject(methodNode.textValue(), args), id);
    }

    @Override
    public Message createResponseMessageFromData(ByteBuffer data, Method method) throws IOException {
        JsonNode node = objectMapper.readTree(new ByteBufferInputStream(data));

        JsonNode jsonrpcNode = node.get("jsonrpc");
        if (jsonrpcNode == null || !jsonrpcNode.isTextual()) {
            throw new IOException("Not a jsonrpc protocol");
        }

        if (!"2.0".equals(jsonrpcNode.textValue())) {
            throw new IOException("Not a jsonrpc 2.0");
        }

        JsonNode resultNode = node.get("result");
        JsonNode errorNode = node.get("error");

        if (resultNode == null && errorNode == null) {
            throw new IOException("Neither 'error' nor 'result' member in the response object");
        }

        if (resultNode != null) {
            return new JsonRpcMessage(this,
                    new Message.ResponseObject(
                            objectMapper.treeToValue(resultNode, method.getReturnType()), false));
        } else {
            JsonRpcError error = objectMapper.treeToValue(errorNode, JsonRpcError.class);
            return new JsonRpcMessage(this,
                    new Message.ResponseObject(
                            new GenericRemoteException(error.getMessage(), error.getCode(), error.getData()),
                            true));
        }
    }

    @Override
    public Message createRequestMessage(String methodName) {
        return new JsonRpcMessage(this, methodName, getNextId());
    }

    @Override
    public Message createResponseMessage(Message requestMessage) {
        return new JsonRpcMessage((JsonRpcMessage) requestMessage, false);
    }

    @Override
    public Message createRequestMessage(Message.RequestObject request) throws IOException {
        return new JsonRpcMessage(this, request, getNextId());
    }

    @Override
    public Message createResponseMessage(Message.ResponseObject response) {
        return new JsonRpcMessage(this, response);
    }

    @Override
    public void sendMessageSync(Message outMsg, Message inMsg) throws IOException {
    }

    @Override
    public InterfaceCodeGen getInterfaceCodeGen() {
        return this;
    }

    @Override
    public <T> T generateInterfaceImpl(Class<T> interfaceClass, InterfaceMapping<T> mapping) {
        Object impl = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass, RemoteInterface.class},
                new JsonRpcInvocationHandler(connection, mapping, this));
        return interfaceClass.cast(impl);
    }

    public ByteBuffer convertMessageToData(JsonRpcMessage message) throws IOException {
        ByteBuffer buf = null;
        switch (message.getMessageKind()) {
            case REQUEST: {
                Message.RequestObject ro = message.getRequestObject();
                JsonRpcHeader header = new JsonRpcHeader(message.getMethodName(), ro.args, message.getId());

                buf = ByteBuffer.wrap(objectMapper.writeValueAsBytes(header));
            }
            break;
            case RESPONSE: {
                Message.ResponseObject ro = message.getResponseObject();
                JsonRpcHeader header = new JsonRpcHeader(ro.result, message.getId());

                buf = ByteBuffer.wrap(objectMapper.writeValueAsBytes(header));
            }
            break;
            case EXCEPTION: {
                Message.ResponseObject ro = message.getResponseObject();
                // FIXME process errors correctly
                JsonRpcHeader header = new JsonRpcHeader(ro.result, message.getId());

                buf = ByteBuffer.wrap(objectMapper.writeValueAsBytes(header));
            }
            break;
        }
        return buf;
    }

}

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

import static de.dfki.kiara.jsonrpc.JsonRpcProtocol.parseMessageId;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.reflect.TypeToken;

import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Message;
import de.dfki.kiara.MessageDeserializationException;
import de.dfki.kiara.Protocol;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcMessage implements Message {

    private final JsonRpcProtocol protocol;
    private final JsonRpcMessage requestMessage;
    private Message.Kind kind;
    private final String methodName;
    private ResponseObject response;
    private RequestObject request;
    private Object id;

    private JsonNode body;
    /**
     * refers to "params" on request, "result" on successful response, and to
     * "data" of the error on error response
     */
    private JsonNode params;

    /**
     * refers to "error on error response
     */
    private JsonNode error;

    public JsonRpcMessage(JsonRpcProtocol protocol, JsonNode node) throws IOException {
        this.body = node;
        if (this.body == null) {
            throw new IOException("Not a jsonrpc protocol");
        }
        JsonNode jsonrpcNode = body.get("jsonrpc");
        if (jsonrpcNode == null || !jsonrpcNode.isTextual()) {
            throw new IOException("Not a jsonrpc protocol");
        }

        if (!"2.0".equals(jsonrpcNode.textValue())) {
            throw new IOException("Not a jsonrpc 2.0");
        }

        JsonNode methodNode = body.get("method");

        if (methodNode != null) {
            if (!methodNode.isTextual()) {
                throw new IOException("Member 'method' in request object is not a string");
            }

            this.kind = Message.Kind.REQUEST;
            this.methodName = methodNode.textValue();
            this.params = body.get("params");
            this.id = parseMessageId(body);
        } else {
            JsonNode resultNode = body.get("result");
            JsonNode errorNode = body.get("error");

            if (resultNode == null && errorNode == null) {
                throw new IOException("Neither 'method' nor 'error' nor 'result' member in the object");
            }

            this.methodName = null;

            if (resultNode != null) {
                this.kind = Kind.RESPONSE;
                this.params = resultNode;
                this.error = null;
            } else {
                this.kind = Kind.EXCEPTION;
                this.error = errorNode;
                this.params = errorNode.get("data");

                JsonRpcError jsonRpcError = protocol.getObjectReader().treeToValue(errorNode, JsonRpcError.class);
                this.response = new Message.ResponseObject(
                        new GenericRemoteException(jsonRpcError.getMessage(), jsonRpcError.getCode(), jsonRpcError.getData()),
                        true);
            }
            this.id = parseMessageId(body);
        }
        this.requestMessage = null;
        this.protocol = protocol;
    }

    private static JsonNode readFromBuffer(JsonRpcProtocol protocol, ByteBuffer data) throws IOException {
        byte[] array;
        int arrayOffset;
        int arrayLength;
        int oldPos = data.position();
        if (data.hasArray()) {
            array = data.array();
            arrayOffset = data.arrayOffset();
            arrayLength = data.remaining();
        } else {
            array = new byte[data.remaining()];
            data.get(array);
            arrayOffset = 0;
            arrayLength = array.length;
        }
        data.position(oldPos);

        JsonNode node;
        try (JsonParser parser = protocol.getObjectReader().getFactory().createParser(array, arrayOffset, arrayLength)) {
            node = parser.readValueAsTree();
        }
        return node;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, ByteBuffer data) throws IOException {
        this(protocol, readFromBuffer(protocol, data));
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, ResponseObject response, JsonRpcMessage requestMessage) {
        this.protocol = protocol;
        this.requestMessage = requestMessage;
        this.methodName = null;
        this.kind = response.isException ? Kind.EXCEPTION : Kind.RESPONSE;
        this.request = null;
        this.response = response;
        this.id = requestMessage.getId();
        this.params = null;
        this.error = null;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, RequestObject request, Object id) {
        this.protocol = protocol;
        this.requestMessage = null;
        this.methodName = request.methodName;
        this.kind = Kind.REQUEST;
        this.request = request;
        this.response = null;
        this.id = id;
        this.params = null;
        this.error = null;
    }

    public Object getId() {
        return id;
    }


    @Override
    public Message getRequestMessage() {
        return requestMessage;
    }

    @Override
    public Kind getMessageKind() {
        return this.kind;
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public JsonNode getParams() {
        return params;
    }

    @Override
    public ByteBuffer getMessageData() throws IOException {
        ByteBuffer buf = null;
        switch (getMessageKind()) {
            case REQUEST: {
                if (this.request == null) {
                    throw new NullPointerException("this.request");
                }
                JsonRpcHeader header = new JsonRpcHeader(getMethodName(), this.request.args, getId());

                buf = ByteBuffer.wrap(protocol.getObjectWriter().writeValueAsBytes(header));
            }
            break;
            case RESPONSE: {
                if (this.response == null) {
                    throw new NullPointerException("this.response");
                }
                JsonRpcHeader header = new JsonRpcHeader(this.response.result, getId());

                buf = ByteBuffer.wrap(protocol.getObjectWriter().writeValueAsBytes(header));
            }
            break;
            case EXCEPTION: {
                if (this.response == null) {
                    throw new NullPointerException("this.response");
                }

                int code;
                String message;
                Object data = null;

                if (this.response.result instanceof GenericRemoteException) {
                    GenericRemoteException ex = (GenericRemoteException)this.response.result;
                    code = ex.getErrorCode();
                    message = ex.getMessage();
                } else if (this.response.result instanceof IllegalArgumentException) {
                    IllegalArgumentException ex = (IllegalArgumentException)this.response.result;
                    code = JsonRpcError.INVALID_PARAMS;
                    message = ex.getMessage();
                } else {
                    code = JsonRpcError.INTERNAL_ERROR;
                    message = this.response.result.toString();
                    data = this.response.result;
                }

                final JsonRpcError jsonRpcError = new JsonRpcError(code, message, data);
                final JsonRpcHeader header = new JsonRpcHeader(jsonRpcError, getId());
                buf = ByteBuffer.wrap(protocol.getObjectWriter().writeValueAsBytes(header));
            }
            break;
        }
        return buf;
    }

    @Override
    public void setGenericError(int errorCode, String errorMessage) {
        this.kind = Kind.EXCEPTION;
        this.response = new ResponseObject(new GenericRemoteException(errorMessage, errorCode), true);
    }

    @Override
    public RequestObject getRequestObject(TypeToken<?>[] paramTypes) throws MessageDeserializationException {
        if (this.request != null) {
            return this.request;
        }

        if (this.kind != Kind.REQUEST) {
            throw new IllegalStateException("not a request message");
        }

        Object[] args = null;
        if (params != null) {
            if (!params.isArray() && !params.isObject()) {
                throw new MessageDeserializationException("Member 'params' is neither array nor object");
            }

            if (params.isArray()) {
                args = new Object[paramTypes.length];

                int i = 0; // parameter index
                final int numParams = params.size();
                final int numParamTypes = paramTypes.length;
                final ObjectReader reader = protocol.getObjectReader();
                for (int j = 0; j < numParamTypes; ++j) {
                    if (paramTypes[j] == null) // this parameter will be set later
                        continue;
                    if (i >= numParams)
                        throw new MessageDeserializationException("Parameter index "+i+" is out of bounds, 'params' size is: " + numParams);
                    try {
                        final JsonParser parser = reader.treeAsTokens(params.get(i));
                        args[j] = reader.withType(paramTypes[j].getType()).readValue(parser);
                    } catch (JsonProcessingException ex) {
                        throw new MessageDeserializationException(ex);
                    } catch (IOException ex) {
                        throw new MessageDeserializationException(ex);
                    }
                    ++i;
                }

                if (i != numParams)
                    throw new MessageDeserializationException("Deserialzed "+i+" parameters, but required "+numParams);

            } else {
                throw new UnsupportedOperationException("Object is not supported as 'params' member");
            }
        }
        this.request = new RequestObject(methodName, args);
        return this.request;
    }

    @Override
    public ResponseObject getResponseObject(TypeToken<?> returnType) throws MessageDeserializationException {
        if (this.response != null) {
            return this.response;
        }

        if (this.kind != Kind.RESPONSE && this.kind != Kind.EXCEPTION) {
            throw new IllegalStateException("not a response message");
        }

        if (this.kind == Kind.RESPONSE) {
            try {
                final ObjectReader reader = protocol.getObjectReader();
                final JsonParser parser = reader.treeAsTokens(this.params);
                final Object result = reader.withType(returnType.getType()).readValue(parser);
                this.response = new Message.ResponseObject(result, false);
                return this.response;
            } catch (JsonProcessingException ex) {
                throw new MessageDeserializationException(ex);
            } catch (IOException ex) {
                throw new MessageDeserializationException(ex);
            }
        } else {
            JsonRpcError jsonRpcError;
            try {
                jsonRpcError = protocol.getObjectReader().treeToValue(this.error, JsonRpcError.class);
            } catch (JsonProcessingException ex) {
                throw new MessageDeserializationException(ex);
            }
            this.response = new Message.ResponseObject(
                    new GenericRemoteException(jsonRpcError.getMessage(), jsonRpcError.getCode(), jsonRpcError.getData()),
                    true);
            return this.response;
        }
    }

    @Override
    public String toString() {
        return "JsonRpcMessage("+kind+", "+getMethodName()+", "+getParams()+")";
    }

}

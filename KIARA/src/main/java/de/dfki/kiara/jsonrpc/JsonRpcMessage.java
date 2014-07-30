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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Message;
import de.dfki.kiara.MessageDeserializationException;
import de.dfki.kiara.Protocol;

import static de.dfki.kiara.jsonrpc.JsonRpcProtocol.parseMessageId;

import de.dfki.kiara.util.ByteBufferInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcMessage implements Message {

    private final JsonRpcProtocol protocol;
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

    public JsonRpcMessage(JsonRpcProtocol protocol, Message.Kind kind, JsonNode node) throws IOException {
        this.body = node;
        JsonNode jsonrpcNode = body.get("jsonrpc");
        if (jsonrpcNode == null || !jsonrpcNode.isTextual()) {
            throw new IOException("Not a jsonrpc protocol");
        }

        if (!"2.0".equals(jsonrpcNode.textValue())) {
            throw new IOException("Not a jsonrpc 2.0");
        }

        if (kind == Kind.REQUEST) {
            JsonNode methodNode = body.get("method");
            if (methodNode == null) {
                throw new IOException("No 'method' member in the request object");
            }

            if (!methodNode.isTextual()) {
                throw new IOException("Member 'method' in request object is not a string");
            }

            this.kind = kind;
            this.methodName = methodNode.textValue();
            this.params = body.get("params");
            this.id = parseMessageId(body);
        } else {
            JsonNode resultNode = body.get("result");
            JsonNode errorNode = body.get("error");

            if (resultNode == null && errorNode == null) {
                throw new IOException("Neither 'error' nor 'result' member in the response object");
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

                JsonRpcError jsonRpcError = protocol.getObjectMapper().treeToValue(errorNode, JsonRpcError.class);
                this.response = new Message.ResponseObject(
                        new GenericRemoteException(jsonRpcError.getMessage(), jsonRpcError.getCode(), jsonRpcError.getData()),
                        true);
            }
            this.id = parseMessageId(body);
        }
        this.protocol = protocol;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, Message.Kind kind, ByteBuffer data) throws IOException {
        this(protocol, kind, protocol.getObjectMapper().readTree(new ByteBufferInputStream(data)));
    }

    /**
     * Create request message.
     *
     * @param protocol
     * @param methodName
     * @param id
     * @param params
     */
    public JsonRpcMessage(JsonRpcProtocol protocol, String methodName, Object id, JsonNode params) {
        this.protocol = protocol;
        this.methodName = methodName;
        this.kind = Kind.REQUEST;
        this.request = null;
        this.response = null;
        this.id = id;
        this.params = params;
        this.error = null;
    }

    public JsonRpcMessage(JsonRpcMessage requestMessage, boolean isException) {
        this.protocol = requestMessage.protocol;
        this.methodName = null;
        this.kind = isException ? Kind.EXCEPTION : Kind.RESPONSE;
        this.request = null;
        this.response = null;
        this.id = requestMessage.id;
        this.params = null;
        this.error = null;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, Message.Kind kind) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = kind;
        this.request = null;
        this.response = null;
        this.id = null;
        this.params = null;
        this.error = null;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, ResponseObject response, Object id) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = response.isException ? Kind.EXCEPTION : Kind.RESPONSE;
        this.request = null;
        this.response = response;
        this.id = id;
        this.params = null;
        this.error = null;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, RequestObject request, Object id) {
        this.protocol = protocol;
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

                buf = ByteBuffer.wrap(protocol.getObjectMapper().writeValueAsBytes(header));
            }
            break;
            case RESPONSE: {
                if (this.response == null) {
                    throw new NullPointerException("this.response");
                }
                JsonRpcHeader header = new JsonRpcHeader(this.response.result, getId());

                buf = ByteBuffer.wrap(protocol.getObjectMapper().writeValueAsBytes(header));
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
                buf = ByteBuffer.wrap(protocol.getObjectMapper().writeValueAsBytes(header));
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
    public RequestObject getRequestObject(Class<?>[] paramTypes) throws MessageDeserializationException {
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
                if (paramTypes.length != params.size()) {
                    throw new MessageDeserializationException("Member 'params' size is: " + params.size() + ", required " + paramTypes.length);
                }

                args = new Object[paramTypes.length];

                for (int i = 0; i < params.size(); ++i) {
                    try {
                        args[i] = protocol.getObjectMapper().treeToValue(params.get(i), paramTypes[i]);
                    } catch (JsonProcessingException ex) {
                        throw new MessageDeserializationException(ex);
                    }
                }
            } else {
                throw new UnsupportedOperationException("Object is not supported as 'params' member");
            }
        }
        return new RequestObject(methodName, args);
    }

    @Override
    public ResponseObject getResponseObject(Class<?> returnType) throws MessageDeserializationException {
        if (this.response != null) {
            return this.response;
        }

        if (this.kind != Kind.RESPONSE && this.kind != Kind.EXCEPTION) {
            throw new IllegalStateException("not a response message");
        }

        if (this.kind == Kind.RESPONSE) {
            try {
                return new Message.ResponseObject(
                        protocol.getObjectMapper().treeToValue(this.params, returnType), false);
            } catch (JsonProcessingException ex) {
                throw new MessageDeserializationException(ex);
            }
        } else {
            JsonRpcError jsonRpcError;
            try {
                jsonRpcError = protocol.getObjectMapper().treeToValue(this.error, JsonRpcError.class);
            } catch (JsonProcessingException ex) {
                throw new MessageDeserializationException(ex);
            }
            return new Message.ResponseObject(
                    new GenericRemoteException(jsonRpcError.getMessage(), jsonRpcError.getCode(), jsonRpcError.getData()),
                    true);
        }
    }

}

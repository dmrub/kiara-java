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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;
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
    private AtomicLong nextId = new AtomicLong(1);

    public JsonRpcProtocol() {
        this.connection = null;
    }

    public long getNextId() {
        return nextId.getAndIncrement();
    }

    @Override
    public void initConnection(Connection connection) {
        if (connection == null)
            throw new IllegalArgumentException("connection can't be null");
        if (this.connection != null)
            throw new IllegalStateException("connection was already initialized");
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
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(new ByteBufferInputStream(data));

        DoubleNode jsonrpcNode = ((DoubleNode)node.get("jsonrpc"));
        if (jsonrpcNode == null)
            throw new IOException("Not a jsonrpc protocol");

        double version = jsonrpcNode.doubleValue();
        if (version != 2.0)
            throw new IOException("Not a jsonrpc 2.0");

        TextNode methodNode = (TextNode)node.get("method");
        if (methodNode == null)
            throw new IOException("No 'method' member in the request object");

        JsonNode paramsNode = node.get("params");

        Object[] args = null;
        if (paramsNode != null) {
            if (!paramsNode.isArray() && !paramsNode.isObject())
                throw new IOException("Member 'params' is neither array nor object");

            if (paramsNode.isArray()) {
                Class<?>[] paramTypes = method.getParameterTypes();

                if (paramTypes.length != paramsNode.size())
                    throw new IOException("Member 'params' size is: "+paramsNode.size()+", required "+paramTypes.length);

                args = new Object[paramTypes.length];

                for (int i = 0; i < paramsNode.size(); ++i) {
                    args[i] = mapper.treeToValue(paramsNode.get(i), paramTypes[i]);
                }
            } else {
                throw new UnsupportedOperationException("Object is not supported as 'params' member");
            }
        }

        JsonNode idNode = node.get("id");
        Object id = null;
        if (idNode != null) {
            if (!idNode.isTextual() && !idNode.isNumber() && !idNode.isNull())
                throw new IOException("Invalid 'id' member");
            if (idNode.isTextual())
                id = idNode.textValue();
            else if (idNode.isNumber())
                id = idNode.numberValue();
        }

        return new JsonRpcMessage(this,
                new Message.RequestObject(methodNode.textValue(), args), id);
    }

    @Override
    public Message createResponseMessageFromData(ByteBuffer data, Method method) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(new ByteBufferInputStream(data));

        DoubleNode jsonrpcNode = ((DoubleNode)node.get("jsonrpc"));
        if (jsonrpcNode == null)
            throw new IOException("Not a jsonrpc protocol");

        double version = jsonrpcNode.doubleValue();
        if (version != 2.0)
            throw new IOException("Not a jsonrpc 2.0");

        JsonNode resultNode = (JsonNode)node.get("result");
        JsonNode errorNode = (JsonNode)node.get("error");

        if (resultNode == null && errorNode == null)
            throw new IOException("Neither 'error' nor 'result' member in the response object");

        if (resultNode != null) {
            return new JsonRpcMessage(this,
                    new Message.ResponseObject(
                            mapper.treeToValue(resultNode, method.getReturnType()), false));
        } else {
            JsonRpcError error = mapper.treeToValue(errorNode, JsonRpcError.class);
            return new JsonRpcMessage(this,
                    new Message.ResponseObject(
                            new GenericRemoteException("JSON-RPC Error", error.getCode(), error.getData()),
                            true));
        }
    }

    @Override
    public Message createRequestMessage(String methodName) {
        return new JsonRpcMessage(this, methodName, getNextId());
    }

    @Override
    public Message createResponseMessage(Message requestMessage) {
        return new JsonRpcMessage((JsonRpcMessage)requestMessage, false);
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

                ObjectMapper mapper = new ObjectMapper();
                buf = ByteBuffer.wrap(mapper.writeValueAsBytes(header));
            }
            break;
            case RESPONSE: {
                Message.ResponseObject ro = message.getResponseObject();
                JsonRpcHeader header = new JsonRpcHeader(ro.result, message.getId());

                ObjectMapper mapper = new ObjectMapper();
                buf = ByteBuffer.wrap(mapper.writeValueAsBytes(header));
            }
            break;
            case EXCEPTION: {
                Message.ResponseObject ro = message.getResponseObject();
                // FIXME process errors correctly
                JsonRpcHeader header = new JsonRpcHeader(ro.result, message.getId());

                ObjectMapper mapper = new ObjectMapper();
                buf = ByteBuffer.wrap(mapper.writeValueAsBytes(header));
            }
            break;
        }
        return buf;
    }

}

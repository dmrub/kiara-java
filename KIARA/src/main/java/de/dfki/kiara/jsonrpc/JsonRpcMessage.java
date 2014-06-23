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

import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcMessage implements Message {

    private final JsonRpcProtocol protocol;
    private final String methodName;
    private Message.Kind kind;
    private ResponseObject response;
    private RequestObject request;
    private Object id;

    public JsonRpcMessage(JsonRpcProtocol protocol, String methodName, Object id) {
        this.protocol = protocol;
        this.methodName = methodName;
        this.kind = Kind.REQUEST;
        this.request = null;
        this.response = null;
        this.id = id;
    }

    public JsonRpcMessage(JsonRpcMessage requestMessage, boolean isException) {
        this.protocol = requestMessage.protocol;
        this.methodName = null;
        this.kind = isException ? Kind.EXCEPTION : Kind.RESPONSE;
        this.request = null;
        this.response = null;
        this.id = requestMessage.id;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, Message.Kind kind) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = kind;
        this.request = null;
        this.response = null;
        this.id = null;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, ResponseObject response) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = Kind.RESPONSE;
        this.request = null;
        this.response = response;
        this.id = null;
    }

    public JsonRpcMessage(JsonRpcProtocol protocol, RequestObject request, Object id) {
        this.protocol = protocol;
        this.methodName = request.methodName;
        this.kind = Kind.REQUEST;
        this.request = request;
        this.response = null;
        this.id = id;
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

    @Override
    public ByteBuffer getMessageData() throws IOException {
        return protocol.convertMessageToData(this);
    }

    @Override
    public RequestObject getRequestObject() {
        return this.request;
    }

    @Override
    public ResponseObject getResponseObject() {
        return this.response;
    }

    @Override
    public void setGenericError(int errorCode, String errorMessage) {
        this.kind = Kind.EXCEPTION;
        this.response = new ResponseObject(new GenericRemoteException(errorMessage, errorCode), true);
    }

}

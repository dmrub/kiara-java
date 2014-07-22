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

package de.dfki.kiara.jos;

import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JosMessage implements Message {

    private final JosProtocol protocol;
    private final String methodName;
    private Message.Kind kind;
    private ResponseObject response;
    private RequestObject request;

    public JosMessage(JosProtocol protocol, String methodName) {
        this.protocol = protocol;
        this.methodName = methodName;
        this.kind = Kind.REQUEST;
        this.request = null;
        this.response = null;
    }

    public JosMessage(JosProtocol protocol, Message.Kind kind) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = kind;
        this.request = null;
        this.response = null;
    }

    public JosMessage(JosProtocol protocol, ResponseObject response) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = Kind.RESPONSE;
        this.request = null;
        this.response = response;
    }

    public JosMessage(JosProtocol protocol, RequestObject request) {
        this.protocol = protocol;
        this.methodName = request.methodName;
        this.kind = Kind.REQUEST;
        this.request = request;
        this.response = null;
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
        NoCopyByteArrayOutputStream os = new NoCopyByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        switch (getMessageKind()) {
            case REQUEST: {
                oos.writeByte(JosProtocol.JOS_REQUEST);
                oos.writeUTF(getMethodName());
                Message.RequestObject ro = getRequestObject(null);
                oos.writeObject(ro.args);
            }
            break;
            case RESPONSE: {
                oos.writeByte(JosProtocol.JOS_RESPONSE);
                Message.ResponseObject ro = getResponseObject(null);
                oos.writeObject(ro.result);
            }
            break;
            case EXCEPTION: {
                oos.writeByte(JosProtocol.JOS_EXCEPTION);
                Message.ResponseObject ro = getResponseObject(null);
                oos.writeObject(ro.result);
            }
            break;
        }
        oos.flush();
        return ByteBuffer.wrap(os.toByteArray(), 0, os.size());
    }

    @Override
    public RequestObject getRequestObject(Class<?>[] paramTypes) {
        return this.request;
    }

    @Override
    public ResponseObject getResponseObject(Class<?> returnType) {
        return this.response;
    }

    @Override
    public void setGenericError(int errorCode, String errorMessage) {
        this.kind = Kind.EXCEPTION;
        this.response = new ResponseObject(new GenericRemoteException(errorMessage, errorCode), true);
    }

}

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
import de.dfki.kiara.util.ByteBufferInputStream;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

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
    private long id;

    public JosMessage(JosProtocol protocol, Kind kind, ByteBuffer data) throws IOException {
        //System.err.println("new JosMessage: " + HexDump.dumpHexString(data));
        try (final ByteBufferInputStream is = new ByteBufferInputStream(data);
             final ObjectInputStream ois = new ObjectInputStream(is)) {
            final byte messageCode = ois.readByte();

            if (kind == Kind.REQUEST) {
                if (messageCode != JosProtocol.JOS_REQUEST) {
                    throw new IOException("Invalid request code: " + messageCode);
                }
                this.id = ois.readLong();
                this.methodName = ois.readUTF();
                try {
                    Object args = ois.readObject();
                    if (args instanceof Object[]) {
                        this.request = new Message.RequestObject(methodName, (Object[]) args);
                    } else if (args instanceof List<?>) {
                        this.request = new Message.RequestObject(methodName, (List<Object>) args);
                    } else {
                        throw new IOException("Could not read request message, arguments are neither array nor List<Object>");
                    }
                } catch (ClassNotFoundException e) {
                    throw new IOException("Could not read request message, arguments are neither array nor List<Object>", e);
                } catch (ClassCastException e) {
                    throw new IOException("Could not read request message", e);
                }
            } else {
                if (messageCode != JosProtocol.JOS_RESPONSE && messageCode != JosProtocol.JOS_EXCEPTION) {
                    throw new IOException("Invalid response code: " + messageCode);
                }
                this.id = ois.readLong();
                this.methodName = null;
                Object result;
                try {
                    result = ois.readObject();
                } catch (ClassNotFoundException e) {
                    throw new IOException("Could not read response message", e);
                }
                this.response = new Message.ResponseObject(result,
                        messageCode == JosProtocol.JOS_EXCEPTION);
            }
            this.protocol = protocol;
        }
    }

    public JosMessage(JosProtocol protocol, String methodName, long id) {
        this.protocol = protocol;
        this.methodName = methodName;
        this.kind = Kind.REQUEST;
        this.request = null;
        this.response = null;
        this.id = id;
    }

    public JosMessage(JosProtocol protocol, Message.Kind kind) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = kind;
        this.request = null;
        this.response = null;
        this.id = -1;
    }

    public JosMessage(JosProtocol protocol, ResponseObject response, long id) {
        this.protocol = protocol;
        this.methodName = null;
        this.kind = response.isException ? Kind.EXCEPTION : Kind.RESPONSE;
        this.request = null;
        this.response = response;
        this.id = id;
    }

    public JosMessage(JosProtocol protocol, RequestObject request, long id) {
        this.protocol = protocol;
        this.methodName = request.methodName;
        this.kind = Kind.REQUEST;
        this.request = request;
        this.response = null;
        this.id = id;
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

    public long getId() {
        return id;
    }

    @Override
    public ByteBuffer getMessageData() throws IOException {
        try (NoCopyByteArrayOutputStream os = new NoCopyByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);) {
            switch (getMessageKind()) {
                case REQUEST: {
                    oos.writeByte(JosProtocol.JOS_REQUEST);
                    oos.writeLong(id);
                    oos.writeUTF(getMethodName());
                    Message.RequestObject ro = getRequestObject(null);
                    oos.writeObject(ro.args);
                }
                break;
                case RESPONSE: {
                    oos.writeByte(JosProtocol.JOS_RESPONSE);
                    oos.writeLong(id);
                    Message.ResponseObject ro = getResponseObject(null);
                    oos.writeObject(ro.result);
                }
                break;
                case EXCEPTION: {
                    oos.writeByte(JosProtocol.JOS_EXCEPTION);
                    oos.writeLong(id);
                    Message.ResponseObject ro = getResponseObject(null);
                    oos.writeObject(ro.result);
                }
                break;
            }
            oos.flush();
            return ByteBuffer.wrap(os.toByteArray(), 0, os.size());
        }
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

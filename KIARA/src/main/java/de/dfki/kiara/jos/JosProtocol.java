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

import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceCodeGen;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.RemoteInterface;
import de.dfki.kiara.util.ByteBufferInputStream;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JosProtocol implements Protocol, InterfaceCodeGen {

    public final static int JOS_REQUEST = 0;
    public final static int JOS_RESPONSE = 1;
    public final static int JOS_EXCEPTION = 2;

    private Connection connection;

    public JosProtocol() {
        this.connection = null;
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
        return "application/octet-stream";
    }

    @Override
    public InterfaceCodeGen getInterfaceCodeGen() {
        return this;
    }

    @Override
    public Message createRequestMessageFromData(ByteBuffer data) throws IOException {
        ByteBufferInputStream is = new ByteBufferInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(is);
        byte requestCode = ois.readByte();
        if (requestCode != JOS_REQUEST) {
            throw new IOException("Invalid request code: " + requestCode);
        }
        String methodName;
        Object[] args;
        try {
            methodName = ois.readUTF();
            args = (Object[]) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Could not read response message", e);
        } catch (ClassCastException e) {
            throw new IOException("Could not read response message", e);
        }
        return new JosMessage(this, new Message.RequestObject(methodName, args));

    }

    @Override
    public Message createResponseMessageFromData(ByteBuffer data) throws IOException {
        ByteBufferInputStream is = new ByteBufferInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(is);
        byte responseCode = ois.readByte();
        if (responseCode != JOS_RESPONSE && responseCode != JOS_EXCEPTION) {
            throw new IOException("Invalid response code: " + responseCode);
        }
        Object result;
        try {
            result = ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Could not read response message", e);
        }
        return new JosMessage(this,
                new Message.ResponseObject(result,
                        responseCode == JOS_EXCEPTION));
    }

    @Override
    public Message createRequestMessage(String methodName) {
        return new JosMessage(this, methodName);
    }

    @Override
    public Message createResponseMessage(Message requestMessage) {
        return new JosMessage(this, Message.Kind.RESPONSE);
    }

    @Override
    public <T> T generateInterfaceImpl(Class<T> interfaceClass, InterfaceMapping<T> mapping) {
        Object impl = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass, RemoteInterface.class},
                new JosInvocationHandler(connection, mapping, this));
        return interfaceClass.cast(impl);
    }

    @Override
    public Message createRequestMessage(Message.RequestObject request) {
        return new JosMessage(this, request);
    }

    @Override
    public Message createResponseMessage(Message.ResponseObject response) {
        return new JosMessage(this, response);
    }

}

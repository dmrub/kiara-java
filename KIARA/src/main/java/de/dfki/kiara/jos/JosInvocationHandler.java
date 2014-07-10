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

import de.dfki.kiara.impl.*;
import com.google.common.reflect.AbstractInvocationHandler;
import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.Message;
import de.dfki.kiara.Util;
import de.dfki.kiara.WrappedRemoteException;
import java.lang.reflect.Method;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JosInvocationHandler extends AbstractInvocationHandler {

    private final Connection connection;
    private final InterfaceMapping<?> interfaceMapping;
    private final JosProtocol protocol;

    public JosInvocationHandler(Connection connection, InterfaceMapping<?> interfaceMapping, JosProtocol protocol) {
        this.connection = connection;
        this.interfaceMapping = interfaceMapping;
        this.protocol = protocol;
    }

    public InterfaceMapping<?> getInterfaceMapping() {
        return interfaceMapping;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] os) throws Throwable {
        if (method.equals(SpecialMethods.riGetConnection)) {
            return getConnection();
        }

        InterfaceMapping<?> mapping = getInterfaceMapping();

        final String idlMethodName = mapping.getIDLMethodName(method);
        if (idlMethodName == null) {
            throw new UnsupportedOperationException("Unknown method: " + method);
        }

        if (Util.isSerializer(method)) {
            return protocol.createRequestMessage(new Message.RequestObject(idlMethodName, os));
        } else if (Util.isDeserializer(method)) {
            Message msg = (Message) os[0];

            Message.ResponseObject ro = msg.getResponseObject();

            if (ro.isException) {
                if (ro.result instanceof Exception) {
                    throw (Exception) ro.result;
                }
                throw new WrappedRemoteException(ro.result);
            }

            return ro.result;
        } else {

            // FIXME this is a hack
            if (method.getReturnType().equals(int.class)) {
                return 0;
            }

            return null;
        }
    }

}

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
import de.dfki.kiara.jsonrpc.DefaultInvocationHandler;
import de.dfki.kiara.jsonrpc.JsonRpcMessage;
import de.dfki.kiara.jsonrpc.JsonRpcMessageDispatcher;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.util.MessageDecoder;
import de.dfki.kiara.util.MessageDispatcher;

import java.lang.reflect.Method;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JosInvocationHandler extends DefaultInvocationHandler<JosProtocol> {

    public JosInvocationHandler(Connection connection, InterfaceMapping<?> interfaceMapping, JosProtocol protocol) {
        super(connection, interfaceMapping, protocol, new MessageDecoder<>(protocol));
    }

    @Override
    public MessageDispatcher createMessageDispatcher(Message request) {
        return new JosMessageDispatcher(protocol, ((JosMessage) request).getId());
    }
}

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
package de.dfki.kiara.jos;

import de.dfki.kiara.ConnectionBase;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.Message;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.impl.ConnectionImpl;
import de.dfki.kiara.impl.DefaultInvocationHandler;
import de.dfki.kiara.impl.ServiceMethodBinding;
import de.dfki.kiara.util.MessageDispatcher;


/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JosInvocationHandler extends DefaultInvocationHandler<JosProtocol> {

    public JosInvocationHandler(ConnectionBase connection, InterfaceMapping<?> interfaceMapping, ServiceMethodBinding serviceMethodBinding, JosProtocol protocol) {
        super(connection, interfaceMapping, serviceMethodBinding, protocol);
    }

    @Override
    public MessageDispatcher createMessageDispatcher(Message request) {
        return new JosMessageDispatcher(protocol, ((JosMessage) request).getId());
    }
}

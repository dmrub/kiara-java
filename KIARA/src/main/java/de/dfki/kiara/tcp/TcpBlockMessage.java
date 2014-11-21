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
package de.dfki.kiara.tcp;

import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpBlockMessage extends TransportMessage {

    private final Map<String, Object> properties = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TcpBlockMessage.class);

    /**
     * @param connection
     * @param payload
     */
    public TcpBlockMessage(Transport connection, ByteBuffer payload) {
        super(connection, payload);
    }

    @Override
    public TransportMessage set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public Object get(String name) {
        return properties.get(name);
    }

    @Override
    public TransportAddress getLocalTransportAddress() {
        try {
            final Transport connection = getConnection();
            final TcpBlockTransportFactory transport = (TcpBlockTransportFactory) connection.getTransportFactory();
            final InetSocketAddress sa = ((InetSocketAddress) connection.getLocalAddress());
            return new TcpBlockAddress(transport, sa.getHostName(), sa.getPort());
        } catch (UnknownHostException ex) {
            logger.error("Could not create transport address", ex);
            return null;
        }
    }

}

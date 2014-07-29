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
package de.dfki.kiara.tcp;

import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.http.HttpAddress;
import de.dfki.kiara.http.HttpTransport;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpBlockMessage extends TransportMessage {

    private final Map<String, Object> properties = new HashMap<>();

    /**
     *
     * @param connection
     * @param payload
     */
    public TcpBlockMessage(TransportConnection connection, ByteBuffer payload) {
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
            final TransportConnection connection = getConnection();
            final HttpTransport transport = (HttpTransport)connection.getTransport();
            final InetSocketAddress sa = ((InetSocketAddress)connection.getLocalAddress());
            return new HttpAddress(transport, sa.getHostName(), sa.getPort(), null);
        } catch (UnknownHostException ex) {
            return null;
        } catch (InvalidAddressException ex) {
            return null;
        } catch (URISyntaxException ex) {
            return null;
        }
    }

}

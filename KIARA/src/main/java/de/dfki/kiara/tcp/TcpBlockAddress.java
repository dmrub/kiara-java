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

import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpBlockAddress implements TransportAddress {

    private final TcpBlockTransport transport;
    private final String hostName;
    private final int port;
    private final InetAddress address;

    public TcpBlockAddress(TcpBlockTransport transport, URI uri) throws InvalidAddressException, UnknownHostException {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (!"tcp".equals(uri.getScheme()) && !"tcps".equals(uri.getScheme())) {
            throw new InvalidAddressException("only tcp and tcps scheme is allowed");
        }

        this.transport = transport;
        this.hostName = uri.getHost();
        this.port = uri.getPort();
        this.address = InetAddress.getByName(this.hostName);
    }

    public TcpBlockAddress(TcpBlockTransport transport, String hostName, int port) throws UnknownHostException {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (hostName == null) {
            throw new NullPointerException("hostName");
        }

        this.transport = transport;
        this.hostName = hostName;
        this.port = port;
        this.address = InetAddress.getByName(hostName);
    }

    @Override
    public Transport getTransport() {
        return transport;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public boolean acceptsTransportConnection(TransportAddress transportAddress) {
        if (transportAddress == null) {
            throw new NullPointerException("transportAddress");
        }
        if (!(transportAddress instanceof TcpBlockAddress)) {
            return false;
        }
        TcpBlockAddress other = (TcpBlockAddress) transportAddress;
        if (!other.address.equals(this.address)) {

            final String otherHostName = other.getHostName();
            final String thisHostName = getHostName();

            if (!otherHostName.equals(thisHostName) && !"0.0.0.0".equals(thisHostName)) {
                return false;
            }
        }

        if (other.getPort() != getPort()) {
            return false;
        }

        return true;
    }


    @Override
    public boolean acceptsConnection(TransportAddress transportAddress) {
        return acceptsTransportConnection(transportAddress);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TcpBlockAddress)) {
            return false;
        }

        TcpBlockAddress other = (TcpBlockAddress) obj;

        // FIXME is following always correct ?
        if (other.getTransport() != getTransport()) {
            return false;
        }

        return (other.address.equals(this.address) || other.hostName.equals(this.hostName)) && other.port == this.port;
    }

    @Override
    public String toString() {
        // FIXME what to do with different flavors of tcp (tcp, tcps) ?
        return getTransport().getName() + "://" + hostName + ":" + port;
    }
}

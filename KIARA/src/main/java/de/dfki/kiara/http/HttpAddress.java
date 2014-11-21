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
package de.dfki.kiara.http;

import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.TransportFactory;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.util.Glob;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpAddress implements TransportAddress {

    private final HttpTransportFactory transport;
    private final String hostName;
    private final int port;
    private final String path;
    private final InetAddress address;

    public HttpAddress(HttpTransportFactory transport, URI uri) throws InvalidAddressException, UnknownHostException {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }

        uri = uri.normalize();

        if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
            throw new InvalidAddressException("only http and https scheme is allowed: used scheme: "+uri.getScheme());
        }
        this.transport = transport;
        this.hostName = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.address = InetAddress.getByName(this.hostName);
    }

    public HttpAddress(HttpTransportFactory transport, String hostName, int port, String path) throws InvalidAddressException, UnknownHostException, URISyntaxException  {
        this(transport, new URI(transport.getName(), null, hostName, port, path, null, null));
    }

    @Override
    public TransportFactory getTransportFactory() {
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

    /**
     * Returns null if path is undefined
     */
    public String getPath() {
        return path;
    }

    @Override
    public boolean acceptsTransportConnection(TransportAddress transportAddress) {
        if (transportAddress == null) {
            throw new NullPointerException("transportAddress");
        }
        if (!(transportAddress instanceof HttpAddress)) {
            return false;
        }
        HttpAddress other = (HttpAddress) transportAddress;
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
        if (!acceptsTransportConnection(transportAddress)) {
            return false;
        }

        HttpAddress other = (HttpAddress) transportAddress;

        final String otherPath = other.getPath();
        final String thisPath = getPath();

        if (!otherPath.equals(thisPath) && !Pattern.matches(Glob.convertGlobToRegex(thisPath), otherPath)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HttpAddress)) {
            return false;
        }

        HttpAddress other = (HttpAddress) obj;

        // FIXME is following always correct ?
        if (other.getTransportFactory() != getTransportFactory()) {
            return false;
        }

        return (other.address.equals(this.address) || other.hostName.equals(this.hostName)) && other.port == this.port && other.path.equals(this.path);
    }

    @Override
    public String toString() {
        return getTransportFactory().getName() + "://" + hostName + ":" + port + path;
    }

}

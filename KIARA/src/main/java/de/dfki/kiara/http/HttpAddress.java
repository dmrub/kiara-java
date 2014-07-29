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
package de.dfki.kiara.http;

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
public class HttpAddress implements TransportAddress {

    private final HttpTransport transport;
    private final String hostName;
    private final int port;
    private final String path;
    private final InetAddress address;

    public HttpAddress(HttpTransport transport, URI uri) throws InvalidAddressException, UnknownHostException {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }

        if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
            throw new InvalidAddressException("only http and https scheme is allowed");
        }
        this.transport = transport;
        this.hostName = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.address = InetAddress.getByName(this.hostName);
    }

    public HttpAddress(HttpTransport transport, String hostName, int port, String path) throws UnknownHostException {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (hostName == null) {
            throw new NullPointerException("hostName");
        }
        if (path == null) {
            throw new NullPointerException("path");
        }

        this.transport = transport;
        this.hostName = hostName;
        this.port = port;
        this.address = InetAddress.getByName(hostName);

        // FIXME normalize path
        String tmpPath = path;

        if (tmpPath.isEmpty()) {
            tmpPath = "/";
        } else {
            if (tmpPath.charAt(0) != '/') {
                tmpPath = "/" + tmpPath;
            }
        }
        this.path = tmpPath;
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

    public String getPath() {
        return path;
    }

    @Override
    public boolean acceptConnection(TransportAddress transportAddress) {
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

        final String otherPath = other.getPath();
        final String thisPath = getPath();

        if (!otherPath.equals(thisPath) && !"*".equals(thisPath)) {
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
        if (other.getTransport() != getTransport()) {
            return false;
        }

        return (other.address.equals(this.address) || other.hostName.equals(this.hostName)) && other.port == this.port && other.path.equals(this.path);
    }

    @Override
    public String toString() {
        return getTransport().getName() + "://" + hostName + ":" + port + path;
    }
}

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
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
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

        uri = uri.normalize();

        if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
            throw new InvalidAddressException("only http and https scheme is allowed");
        }
        this.transport = transport;
        this.hostName = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.address = InetAddress.getByName(this.hostName);
    }

    public HttpAddress(HttpTransport transport, String hostName, int port, String path) throws InvalidAddressException, UnknownHostException, URISyntaxException  {
        this(transport, new URI(transport.getName(), null, hostName, port, path, null, null));
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

    /**
     * Returns null if path is undefined
     */
    public String getPath() {
        return path;
    }

    /**
     * From http://stackoverflow.com/questions/1247772
     *
     * Converts a standard POSIX Shell globbing pattern into a regular
     * expression pattern. The result can be used with the standard
     * {@link java.util.regex} API to recognize strings which match the glob
     * pattern.
     * <p/>
     * See also, the POSIX Shell language:
     * http://pubs.opengroup.org/onlinepubs/009695399/utilities/xcu_chap02.html#tag_02_13_01
     *
     * @param pattern A glob pattern.
     * @return A regex pattern to recognize the given glob pattern.
     */
    private static final String convertGlobToRegex(String pattern) {
        StringBuilder sb = new StringBuilder(pattern.length());
        int inGroup = 0;
        int inClass = 0;
        int firstIndexInClass = -1;
        char[] arr = pattern.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            switch (ch) {
                case '\\':
                    if (++i >= arr.length) {
                        sb.append('\\');
                    } else {
                        char next = arr[i];
                        switch (next) {
                            case ',':
                                // escape not needed
                                break;
                            case 'Q':
                            case 'E':
                                // extra escape needed
                                sb.append('\\');
                            default:
                                sb.append('\\');
                        }
                        sb.append(next);
                    }
                    break;
                case '*':
                    if (inClass == 0) {
                        sb.append(".*");
                    } else {
                        sb.append('*');
                    }
                    break;
                case '?':
                    if (inClass == 0) {
                        sb.append('.');
                    } else {
                        sb.append('?');
                    }
                    break;
                case '[':
                    inClass++;
                    firstIndexInClass = i + 1;
                    sb.append('[');
                    break;
                case ']':
                    inClass--;
                    sb.append(']');
                    break;
                case '.':
                case '(':
                case ')':
                case '+':
                case '|':
                case '^':
                case '$':
                case '@':
                case '%':
                    if (inClass == 0 || (firstIndexInClass == i && ch == '^')) {
                        sb.append('\\');
                    }
                    sb.append(ch);
                    break;
                case '!':
                    if (firstIndexInClass == i) {
                        sb.append('^');
                    } else {
                        sb.append('!');
                    }
                    break;
                case '{':
                    inGroup++;
                    sb.append('(');
                    break;
                case '}':
                    inGroup--;
                    sb.append(')');
                    break;
                case ',':
                    if (inGroup > 0) {
                        sb.append('|');
                    } else {
                        sb.append(',');
                    }
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
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

        if (!otherPath.equals(thisPath) && !Pattern.matches(convertGlobToRegex(thisPath), otherPath)) {
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

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
package de.dfki.kiara;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class TransportRegistry {

    private TransportRegistry() {
    }

    private static final Map<String, Transport> transports = new HashMap<>();

    public static synchronized Transport getTransportByName(String transportName) {
        return transports.get(transportName);
    }

    public static synchronized Transport getTransportByURI(String uri) throws URISyntaxException {
        return getTransportByURI(new URI(uri));
    }

    public static synchronized Transport getTransportByURI(URI uri) {
        String scheme = uri.getScheme();
        if (scheme == null) {
            return null;
        }
        return getTransportByName(scheme);
    }

    public static synchronized void registerTransport(String transportName, Transport transport) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (transportName == null) {
            throw new NullPointerException("transportName");
        }
        transports.put(transportName, transport);
    }

    public static synchronized void registerTransport(Transport transport) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        final String transportName = transport.getName();
        if (transportName == null) {
            throw new NullPointerException("transportName");
        }
        transports.put(transportName, transport);
    }
}

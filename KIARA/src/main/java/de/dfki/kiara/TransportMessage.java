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

import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public abstract class TransportMessage {

    public static final class Names {
        public static final String SESSION_ID = "session-id";
        public static final String CONTENT_TYPE = "content-type";
        public static final String REQUEST_URI = "request-uri";
        public static final String HTTP_METHOD = "http-method";
    }

    private final Transport connection;
    private ByteBuffer payload;

    protected TransportMessage(Transport connection, ByteBuffer payload) {
        if (connection == null)
            throw new NullPointerException("connection");
        this.connection = connection;
        this.payload = payload;
    }

    protected TransportMessage(Transport connection) {
        this(connection, null);
    }

    public abstract TransportAddress getLocalTransportAddress();

    public Transport getConnection() {
        return connection;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    public TransportMessage setPayload(ByteBuffer payload) {
        this.payload = payload;
        return this;
    }

    public boolean hasPayload() {
        return getPayloadSize() > 0;
    }

    public int getPayloadSize() {
        if (payload == null)
            return 0;
        return payload.remaining();
    }

    public Object getSessionId() {
        return get(Names.SESSION_ID);
    }

    public TransportMessage setSessionId(Object sessionId) {
        return set(Names.SESSION_ID, sessionId);
    }

    public String getContentType() {
        return (String)get(Names.CONTENT_TYPE);
    }

    public TransportMessage setContentType(String value) {
        return set(Names.CONTENT_TYPE, value);
    }

    public String getRequestUri() {
        return (String)get(Names.REQUEST_URI);
    }

    public TransportMessage setRequestUri(String uri) {
        return set(Names.REQUEST_URI, uri);
    }

    public String getHttpMethod() {
        return (String)get(Names.HTTP_METHOD);
    }

    public TransportMessage setHttpMethod(String method) {
        return set(Names.HTTP_METHOD, method);
    }

    public abstract TransportMessage set(String name, Object value);

    public abstract Object get(String name);
}

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
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpRequestMessage extends TransportMessage {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpRequestMessage.class);

    private final HttpRequest request;
    private final HttpContent content;

    /**
     *
     * @param connection
     * @param request
     */
    public HttpRequestMessage(Transport connection, FullHttpRequest request) {
        super(connection);
        if (request == null) {
            throw new NullPointerException("request");
        }
        this.request = request;
        this.content = request;
    }

    public HttpRequestMessage(Transport connection, HttpRequest request, HttpContent content) {
        super(connection);
        if (request == null)
            throw new NullPointerException("request");
        if (content == null)
            throw new NullPointerException("content");
        this.request = request;
        this.content = content;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpContent getContent() {
        return content;
    }

    public HttpRequest finalizeRequest() {
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, getPayload().remaining());
        ByteBuf bbuf = Unpooled.wrappedBuffer(getPayload());
        ByteBuf byteContent = content.content().clear();
        byteContent.writeBytes(bbuf);
        bbuf.release();
        return request;
    }

    @Override
    public TransportMessage set(String name, Object value) {
        if (Names.CONTENT_TYPE.equals(name)) {
            request.headers().set(HttpHeaders.Names.CONTENT_TYPE, value);
        } else if (Names.SESSION_ID.equals(name)) {
            request.headers().set("x-kiara-session", value);
        } else if (Names.REQUEST_URI.equals(name)) {
            request.setUri((String)value);
        } else if (Names.HTTP_METHOD.equals(name)) {
            request.setMethod(HttpMethod.valueOf(name));
        }
        return this;
    }

    @Override
    public Object get(String name) {
        if (Names.CONTENT_TYPE.equals(name)) {
            return request.headers().get(HttpHeaders.Names.CONTENT_TYPE);
        } else if (Names.SESSION_ID.equals(name)) {
            return request.headers().get("x-kiara-session");
        } else if (Names.REQUEST_URI.equals(name)) {
            return request.getUri();
        } else if (Names.HTTP_METHOD.equals(name)) {
            return request.getMethod().name();
        }
        return null;
    }

    @Override
    public TransportAddress getLocalTransportAddress() {
        try {
            final Transport connection = getConnection();
            final HttpTransportFactory transport = (HttpTransportFactory)connection.getTransportFactory();
            final InetSocketAddress sa = ((InetSocketAddress)connection.getLocalAddress());
            return new HttpAddress(transport, sa.getHostName(), sa.getPort(), request.getUri());
        } catch (UnknownHostException ex) {
            logger.error("Could not create transport address", ex);
            return null;
        } catch (InvalidAddressException ex) {
            logger.error("Could not create transport address", ex);
            return null;
        } catch (URISyntaxException ex) {
            logger.error("Could not create transport address", ex);
            return null;
        }
    }

}

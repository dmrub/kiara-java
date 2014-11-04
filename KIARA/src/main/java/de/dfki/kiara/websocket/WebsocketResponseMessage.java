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
package de.dfki.kiara.websocket;

import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import io.netty.handler.codec.http.HttpResponse;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class WebsocketResponseMessage extends TransportMessage {

    private final HttpHeaders headers;
    private final HttpResponse response;
    private final HttpContent content;

    WebsocketResponseMessage(TransportConnection connection, HttpHeaders headers) {
        super(connection);
        if (headers == null) {
            throw new NullPointerException("headers");
        }

        this.headers = headers;
        this.response = null;
        this.content = null;
    }

    WebsocketResponseMessage(TransportConnection connection, HttpResponse response, HttpContent content) {
        super(connection);
        if (response == null) {
            throw new NullPointerException("response");
        }
        if (content == null) {
            throw new NullPointerException("content");
        }

        this.response = response;
        this.headers = response.headers();
        this.content = content;
    }

    WebsocketResponseMessage(TransportConnection connection, FullHttpResponse response) {
        super(connection);
        if (response == null) {
            throw new NullPointerException("response");
        }

        this.response = response;
        this.headers = response.headers();
        this.content = response;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public HttpContent getContent() {
        return content;
    }

    public HttpResponse finalizeResponse() {
        HttpResponse httpResponse = getResponse();
        getContent().content().clear();

        if (getPayload() != null) {
            ByteBuf bbuf = Unpooled.wrappedBuffer(getPayload());
            getContent().content().writeBytes(bbuf);
        }

        httpResponse.headers().set(CONTENT_LENGTH, getContent().content().readableBytes());

        return httpResponse;
    }

    @Override
    public TransportMessage set(String name, Object value) {
        if (Names.CONTENT_TYPE.equals(name)) {
            headers.set(HttpHeaders.Names.CONTENT_TYPE, value);
        } else if (Names.SESSION_ID.equals(name)) {
            headers.set("x-kiara-session", value);
        }
        return this;
    }

    @Override
    public Object get(String name) {
        if (Names.CONTENT_TYPE.equals(name)) {
            return headers.get(HttpHeaders.Names.CONTENT_TYPE);
        } else if (Names.SESSION_ID.equals(name)) {
            return headers.get("x-kiara-session");
        }
        return null;
    }

    @Override
    public TransportAddress getLocalTransportAddress() {
        throw new UnsupportedOperationException("response message does not have transport address");
    }

}

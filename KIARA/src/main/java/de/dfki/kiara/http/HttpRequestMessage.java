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

import de.dfki.kiara.TransportMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpRequestMessage extends TransportMessage {

    private final FullHttpRequest request;

    /**
     *
     * @param transport
     * @param request
     */
    public HttpRequestMessage(HttpTransportConnection connection, FullHttpRequest request) {
        super(connection);
        if (request == null) {
            throw new NullPointerException("request");
        }
        this.request = request;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public FullHttpRequest finalizeRequest() {
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, getPayload().remaining());
        ByteBuf bbuf = Unpooled.wrappedBuffer(getPayload());
        ByteBuf content = request.content().clear();
        content.writeBytes(bbuf);
        bbuf.release();
        return request;
    }

    @Override
    public TransportMessage set(String name, Object value) {
        if (Names.CONTENT_TYPE.equals(name)) {
            request.headers().set(HttpHeaders.Names.CONTENT_TYPE, value);
        } else if (Names.SESSION_ID.equals(name)) {
            request.headers().set("x-kiara-session", value);
        }
        return this;
    }

    @Override
    public Object get(String name) {
        if (Names.CONTENT_TYPE.equals(name)) {
            return request.headers().get(HttpHeaders.Names.CONTENT_TYPE);
        } else if (Names.SESSION_ID.equals(name)) {
            return request.headers().get("x-kiara-session");
        }
        return null;
    }

}

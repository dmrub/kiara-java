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

import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private HttpResponseStatus status = null;
    private HttpHeaders headers = null;
    private final NoCopyByteArrayOutputStream bout = new NoCopyByteArrayOutputStream(1024);
    private HttpTransportConnection connection;

    public HttpClientHandler(HttpTransportConnection connection) {
        this.connection = connection;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            status = response.getStatus();
            headers = response.headers();
            //if (!response.headers().isEmpty()) {
            //    contentType = response.headers().get("Content-Type");
            //}
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            if (buf.isReadable()) {
                if (buf.hasArray()) {
                    bout.write(buf.array(), buf.readerIndex(), buf.readableBytes());
                } else {
                    byte[] bytes = new byte[buf.readableBytes()];
                    buf.getBytes(buf.readerIndex(), bytes);
                    bout.write(bytes);
                }
            }
            if (content instanceof LastHttpContent) {
                //ctx.close();
                bout.flush();
                HttpResponseMessage response = new HttpResponseMessage(connection, headers);
                response.setPayload(ByteBuffer.wrap(bout.toByteArray(), 0, bout.size()));
                this.connection.onResponse(response);
                bout.reset();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        this.connection.onErrorResponse(cause);
    }

}

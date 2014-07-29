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
package de.dfki.kiara.tcp;

import de.dfki.kiara.Handler;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.netty.ByteBufferDecoder;
import de.dfki.kiara.netty.ByteBufferEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import java.nio.ByteOrder;

public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final TcpBlockTransport transport;
    private final SslContext sslCtx;
    private final Handler<TransportConnection> connectionHandler;

    public TcpServerInitializer(TcpBlockTransport transport, SslContext sslCtx, Handler<TransportConnection> connectionHandler) {
        this.transport = transport;
        this.sslCtx = sslCtx;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        // Enable TCPS if necessary.
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast("logger", new LoggingHandler(LogLevel.DEBUG));

        p.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, 4, 0, 4, true));
        p.addLast(new ByteBufferDecoder());

        p.addLast(new LengthFieldPrepender(4, 0, false) {
            @Override
            protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
                ByteBuf outWithLittleEndian = out.order(ByteOrder.LITTLE_ENDIAN);
                super.encode(ctx, msg, outWithLittleEndian);
            }
        });
        p.addLast(new ByteBufferEncoder());
        p.addLast(new TcpHandler(transport, connectionHandler));
    }
}

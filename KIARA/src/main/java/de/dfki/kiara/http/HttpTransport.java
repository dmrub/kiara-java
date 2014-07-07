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

import de.dfki.kiara.Kiara;
import de.dfki.kiara.Service;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpTransport implements Transport, Service {

    private final EventLoopGroup group = new NioEventLoopGroup();

    public HttpTransport() {
        Kiara.addRunningService(this);
    }

    @Override
    public void shutdownGracefully() {
        group.shutdownGracefully();
    }

    @Override
    public String getName() {
        return "http";
    }

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public boolean isHttpTransport() {
        return true;
    }

    @Override
    public boolean isSecureTransport() {
        return false;
    }

    @Override
    public TransportAddress createAddress(String uri) {
        return null;
    }

    @Override
    public TransportConnection openConnection(String uri, Map<String, Object> settings) throws IOException, URISyntaxException {
        return openConnection(new URI(uri), settings);
    }

    static class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

        private Throwable error = null;
        private HttpResponseStatus status = null;
        private String contentType = null;

        private final NoCopyByteArrayOutputStream bout = new NoCopyByteArrayOutputStream(1024);

        private HttpTransportConnection connection;

        public HttpClientHandler(HttpTransportConnection connection) {
            this.connection = connection;
        }

        public Throwable getError() {
            return error;
        }

        public byte[] getContent() {
            return bout.toByteArray();
        }

        public int getContentSize() {
            return bout.size();
        }

        public HttpResponseStatus getStatus() {
            return status;
        }

        public String getContentType() {
            return contentType;
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;

                status = response.getStatus();

                if (!response.headers().isEmpty()) {
                    contentType = response.headers().get("Content-Type");
                }
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
                    ctx.close();
                    bout.flush();
                    this.connection.onContent(bout.toByteArray(), 0, bout.size());
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            error = cause;
            ctx.close();
        }
    }

    static class HttpClientInitializer extends ChannelInitializer<SocketChannel> {

        private final SslContext sslCtx;
        private final HttpClientHandler handler;

        public HttpClientInitializer(SslContext sslCtx, HttpClientHandler handler) {
            this.sslCtx = sslCtx;
            this.handler = handler;
        }

        @Override
        public void initChannel(SocketChannel ch) {
            ChannelPipeline p = ch.pipeline();

            // Enable HTTPS if necessary.
            if (sslCtx != null) {
                p.addLast(sslCtx.newHandler(ch.alloc()));
            }

            p.addLast(new HttpClientCodec());

            // Remove the following line if you don't want automatic content decompression.
            p.addLast(new HttpContentDecompressor());

            // Uncomment the following line if you don't want to handle HttpContents.
            //p.addLast(new HttpObjectAggregator(1048576));
            p.addLast(handler);
        }
    }

    public TransportConnection openConnection(URI uri, Map<String, Object> settings) throws IOException {
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IOException("Only HTTP(S) is supported.");
        }

        // Configure SSL context if necessary.
        final boolean ssl = "https".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }

        HttpTransportConnection connection = new HttpTransportConnection(uri);
        // Configure the client.
        HttpClientHandler handler = new HttpClientHandler(connection);
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(sslCtx, handler));
        // Make the connection attempt.
        connection.init(b.connect(host, port));
        return connection;
    }

}

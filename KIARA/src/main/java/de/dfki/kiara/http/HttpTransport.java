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

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.RunningService;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.netty.ChannelFutureAndConnection;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpTransport implements Transport, RunningService {

    private final EventLoopGroup group = new NioEventLoopGroup();

    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

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
    public ListenableFuture<TransportConnection> openConnection(String uri, Map<String, Object> settings) throws IOException, URISyntaxException {
        return openConnection(new URI(uri), settings);
    }

    public ChannelFutureAndConnection connect(URI uri, Map<String, Object> settings) throws IOException {
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

        // Configure the client.
        final HttpClientHandler httpClientHandler = new HttpClientHandler(uri, HttpMethod.POST);
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(sslCtx, httpClientHandler));
        return new ChannelFutureAndConnection(b.connect(host, port), httpClientHandler);
    }

    public ListenableFuture<TransportConnection> openConnection(URI uri, Map<String, Object> settings) throws IOException {
        final ChannelFutureAndConnection cfc = connect(uri, settings);
        return new ListenableConstantFutureAdapter<>(cfc.future, cfc.connection);
    }

}

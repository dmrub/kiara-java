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

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportConnectionListener;
import de.dfki.kiara.netty.AbstractTransport;
import de.dfki.kiara.netty.ChannelFutureAndConnection;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.Map;
import javax.net.ssl.SSLException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class WebsocketTransport extends AbstractTransport {

    private final boolean secure;

    public WebsocketTransport(boolean secure) {
        this.secure = secure;
    }

    @Override
    public String getName() {
        return secure ? "wss" : "ws";
    }

    @Override
    public int getPriority() {
        return secure ? 19 : 20;
    }

    @Override
    public boolean isSecureTransport() {
        return secure;
    }

    @Override
    public boolean isAddressContainsRequestPath() {
        return true;
    }

    @Override
    public TransportAddress createAddress(String uriStr) throws InvalidAddressException, UnknownHostException {
        try {
            return new WebsocketAddress(this, new URI(uriStr));
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    @Override
    public ListenableFuture<TransportConnection> openConnection(String uri, Map<String, Object> settings) throws IOException, InvalidAddressException {
        try {
            return openConnection(new URI(uri), settings);
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    public ChannelFutureAndConnection connect(URI uri, Map<String, Object> settings) throws IOException {
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            throw new IOException("Only WS(S) is supported.");
        }

        // Configure SSL context if necessary.
        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }

        // Configure the client.
        final WebsocketHandler httpClientHandler = new WebsocketHandler(this, uri, HttpMethod.POST);
        Bootstrap b = new Bootstrap();
        b.group(getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new WebsocketClientInitializer(sslCtx, httpClientHandler));
        return new ChannelFutureAndConnection(b.connect(host, port), httpClientHandler);
    }

    public ListenableFuture<TransportConnection> openConnection(URI uri, Map<String, Object> settings) throws IOException {
        final ChannelFutureAndConnection cfc = connect(uri, settings);
        return new ListenableConstantFutureAdapter<>(cfc.future, cfc.connection);
    }

    @Override
    public ChannelHandler createServerChildHandler(TransportConnectionListener connectionListener) {
        try {
            return new WebsocketServerInitializer(this, createServerSslContext(), connectionListener);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

}

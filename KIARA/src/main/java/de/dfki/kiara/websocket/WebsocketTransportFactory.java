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
import com.google.common.util.concurrent.SettableFuture;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportListener;
import de.dfki.kiara.netty.AbstractTransportFactory;
import de.dfki.kiara.netty.ChannelFutureAndConnection;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
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
public class WebsocketTransportFactory extends AbstractTransportFactory {

    private final boolean secure;

    public WebsocketTransportFactory(boolean secure) {
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
        return false;
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
    public ListenableFuture<Transport> openConnection(String uri, Map<String, Object> settings) throws IOException, InvalidAddressException {
        try {
            return openConnection(new URI(uri), settings);
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    private ListenableFuture<Transport> openConnection(URI uri, Map<String, Object> settings) throws IOException {
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
        final SettableFuture<Transport> onConnectionActive = SettableFuture.create();
        final WebsocketHandler clientHandler = new WebsocketHandler(this, uri,
                WebSocketClientHandshakerFactory.newHandshaker(
                        uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()), HttpMethod.POST, onConnectionActive);
        Bootstrap b = new Bootstrap();
        b.group(getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new WebsocketClientInitializer(sslCtx, clientHandler));

        try {
            b.connect(host, port).sync();
            ChannelFuture f = clientHandler.getHandshakeFuture().sync();
            return onConnectionActive;
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public ChannelHandler createServerChildHandler(String path, TransportListener connectionListener) {
        try {
            SettableFuture<Transport> onConnectionActive = null;
            return new WebsocketServerInitializer(this, createServerSslContext(), path, connectionListener, onConnectionActive);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

}

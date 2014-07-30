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

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Handler;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.http.HttpAddress;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpBlockTransport extends AbstractTransport {

    public static final int DEFAULT_TCP_PORT = 1111;
    public static final int DEFAULT_TCPS_PORT = 1112;

    @Override
    public String getName() {
        return "tcp";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean isHttpTransport() {
        return false;
    }

    @Override
    public boolean isSecureTransport() {
        return false;
    }

    @Override
    public TransportAddress createAddress(String uri) throws InvalidAddressException, UnknownHostException {
        if (uri == null)
            throw new NullPointerException("uri");
        try {
            return new TcpBlockAddress(this, new URI(uri));
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    @Override
    public ListenableFuture<TransportConnection> openConnection(String uri, Map<String, Object> settings) throws InvalidAddressException, IOException {
        if (uri == null)
            throw new NullPointerException("uri");
        try {
            return openConnection(new URI(uri), settings);
        } catch (URISyntaxException ex) {
            throw new InvalidAddressException(ex);
        }
    }

    public ChannelFutureAndConnection connect(URI uri, Map<String, Object> settings) throws IOException {
        if (uri == null)
            throw new NullPointerException("uri");

        final String scheme = uri.getScheme();

        if (!"tcp".equalsIgnoreCase(scheme) && !"tcps".equalsIgnoreCase(scheme))
            throw new IllegalArgumentException("URI has neither tcp nor tcps scheme");

        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = DEFAULT_TCP_PORT;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = DEFAULT_TCPS_PORT;
            }
        }

        // Configure SSL context if necessary.
        final boolean ssl = "tcps".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }

        // Configure the client.
        final TcpHandler httpClientHandler = new TcpHandler(uri, HttpMethod.POST);
        Bootstrap b = new Bootstrap();
        b.group(getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new TcpClientInitializer(sslCtx, httpClientHandler));
        return new ChannelFutureAndConnection(b.connect(host, port), httpClientHandler);
    }

    public ListenableFuture<TransportConnection> openConnection(URI uri, Map<String, Object> settings) throws IOException {
        final ChannelFutureAndConnection cfc = connect(uri, settings);
        return new ListenableConstantFutureAdapter<>(cfc.future, cfc.connection);
    }

    @Override
    public ChannelHandler createServerChildHandler(Handler<TransportConnection> connectionHandler) {
        try {
            return new TcpServerInitializer(createServerSslContext(), connectionHandler);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

}

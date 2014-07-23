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
package de.dfki.kiara.impl;

import de.dfki.kiara.Handler;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.RunningService;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportServer;
import de.dfki.kiara.netty.AbstractTransport;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportServerImpl implements TransportServer, RunningService {

    private static class ServerEntry {

        public final String address;
        public final String port;
        public final AbstractTransport transport;
        public final Handler<TransportConnection> handler;
        public Channel channel;

        public ServerEntry(String address, String port, AbstractTransport transport, Handler<TransportConnection> handler) {
            this.address = address;
            this.port = port;
            this.transport = transport;
            this.handler = handler;
            this.channel = null;
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(TransportServerImpl.class);

    private final List<ServerEntry> serverEntries = new ArrayList<>();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();


    public TransportServerImpl() throws CertificateException, SSLException {
        // bossGroup and workerGroup need to be always shutdown, so we are always running service
        Kiara.addRunningService(this);
    }

    @Override
    public void listen(String address, String port, Transport transport, Handler<TransportConnection> handler) {
        if (!(transport instanceof AbstractTransport))
            throw new IllegalArgumentException("transport is not an instance of " + AbstractTransport.class.getName() + " class");
        synchronized (serverEntries) {
            serverEntries.add(new ServerEntry(address, port, (AbstractTransport) transport, handler));
        }
    }

    @Override
    public void run() throws IOException {
        int numServers = 0;
        try {
            synchronized (serverEntries) {
                for (ServerEntry serverEntry : serverEntries) {
                    int port = Integer.parseInt(serverEntry.port);

                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(serverEntry.transport.createServerChildHandler(serverEntry.handler));

                    serverEntry.channel = b.bind(serverEntry.address, port).sync().channel();
                    ++numServers;
                }
            }
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        List<ServerEntry> tmp;
        synchronized (serverEntries) {
            tmp = new ArrayList<>(serverEntries);
            serverEntries.clear();
        }
        for (final ServerEntry serverEntry : tmp) {
            if (serverEntry.channel != null) {
                serverEntry.channel.close();
            }
        }

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    @Override
    public void shutdownGracefully() {
        try {
            close();
        } catch (IOException ex) {
            logger.error("Error on shutdown: {}", ex);
        }
    }

}

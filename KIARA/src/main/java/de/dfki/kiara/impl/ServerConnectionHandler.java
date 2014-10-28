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
package de.dfki.kiara.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import de.dfki.kiara.*;
import de.dfki.kiara.config.ServerConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConnectionHandler extends AbstractMessageConnection {

    private static final Logger logger = LoggerFactory.getLogger(ServerConnectionHandler.class);

    private final ServerImpl server;
    private final List<ServerConnectionImpl> serviceHandlers;
    private final List<MessageListener> listeners;

    public ServerConnectionHandler(ServerImpl server, TransportConnection transportConnection, List<TransportAddressAndServiceHandler> serviceHandlers) {
        super(transportConnection);
        this.server = server;
        this.listeners = new ArrayList<>();

        this.serviceHandlers = new ArrayList<>(serviceHandlers.size());
        for (TransportAddressAndServiceHandler element : serviceHandlers) {
            this.serviceHandlers.add(new ServerConnectionImpl(this, element));
        }
    }

    public List<ServerConnectionImpl> getServiceHandlers() {
        return serviceHandlers;
    }

    @Override
    public void onMessage(TransportMessage trequest) {
        final TransportConnection tconnection = trequest.getConnection();
        final TransportMessage tresponse = tconnection.createResponse(trequest);
        final Transport transport = tconnection.getTransport();
        final String transportName = transport.getName();

        String responseText = null;
        String contentType = null;
        boolean requestProcessed = false;

        // process server configuration request
        if ("http".equalsIgnoreCase(transportName) || "https".equalsIgnoreCase(transportName)) {
            try {
                URI requestUri = new URI(trequest.getRequestUri()).normalize();

                if (server.getConfigUri().getPath().equals(requestUri.getPath())) {
                    ServerConfiguration config = server.generateServerConfiguration(
                            ((InetSocketAddress) tconnection.getLocalAddress()).getHostName(),
                            ((InetSocketAddress) tconnection.getRemoteAddress()).getHostName());

                    responseText = config.toJson();
                    contentType = "application/json";
                    requestProcessed = true;
                }
            } catch (URISyntaxException ex) {
                logger.error("Error", ex);
                responseText = ex.toString();
                contentType = "text/plain; charset=UTF-8";
                requestProcessed = true;
            } catch (IOException ex) {
                logger.error("Error", ex);
                responseText = ex.toString();
                contentType = "text/plain; charset=UTF-8";
                requestProcessed = true;
            }
        }

        if (!requestProcessed) {
            ServerConnectionImpl sc = null;
            if (serviceHandlers.size() > 1 || transport.isAddressContainsRequestPath()) {
                final TransportAddress localTransportAddress = trequest.getLocalTransportAddress();
                for (ServerConnectionImpl element : serviceHandlers) {
                    if (element.getTransportAddress().acceptsConnection(localTransportAddress)) {
                        sc = element;
                    }
                }
            } else if (serviceHandlers.size() == 1) {
                sc = serviceHandlers.get(0);
            }

            if (sc != null) {
                try {
                    processMessage(sc.getProtocol(), sc, sc.getServiceMethodBinding(), trequest);
                    return;
                } catch (Exception ex) {
                    logger.error("Could not process message", ex);
                }
            } else {
                logger.error("No service handler for request: {}", trequest);
                responseText = "No service handler for request";
                contentType = "text/plain; charset=UTF-8";
            }
        }

        try {
            if (responseText != null && contentType != null) {
                tresponse.setPayload(ByteBuffer.wrap(responseText.getBytes("UTF-8")));
                tresponse.setContentType(contentType);
                tconnection.send(tresponse);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error("No UTF-8 encoding", ex);
        }
    }

    void fireClientConnectionOpened(final List<ServerConnectionListener> listeners) {
        Global.executor.execute(new Runnable() {

            @Override
            public void run() {
                synchronized (listeners) {
                    for (ServerConnectionListener listener : listeners) {
                        for (ServerConnection connection : serviceHandlers) {
                            listener.onConnectionOpened(connection);
                        }
                    }
                }
            }
        });
    }

    void fireClientConnectionClosed(final List<ServerConnectionListener> listeners) {
        Global.executor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (listeners) {
                    for (ServerConnectionListener listener : listeners) {
                        for (ServerConnection connection : serviceHandlers) {
                            listener.onConnectionClosed(connection);
                        }
                    }
                }
            }
        });
    }

    public final ListenableFuture<Message> performRemoteAsyncCall(Message request, ListeningExecutorService executor) throws IOException {
        return AsyncCall.performRemoteAsyncCall(this, request, executor);
    }

}

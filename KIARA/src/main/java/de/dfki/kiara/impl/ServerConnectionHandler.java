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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.*;
import de.dfki.kiara.config.ServerConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConnectionHandler implements RequestHandler<TransportMessage, ListenableFuture<TransportMessage>>, Connection {

    private static final Logger logger = LoggerFactory.getLogger(ServerConnectionHandler.class);

    private final ServerImpl server;
    private final TransportConnection transportConnection;
    private final ServiceHandler serviceHandler;

    public ServerConnectionHandler(ServerImpl server, TransportConnection transportConnection, ServiceHandler serviceHandler) {
        this.server = server;
        this.transportConnection = transportConnection;
        this.serviceHandler = serviceHandler;
    }

    public ServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public TransportConnection getTransportConnection() {
        return transportConnection;
    }

    @Override
    public <T> T getServiceInterface(MethodBinding<T> methodBinding) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ListenableFuture<TransportMessage> onRequest(TransportMessage request) throws Exception {
        final TransportConnection connection = request.getConnection();
        final TransportMessage response = connection.createResponse(request);
        final Transport transport = connection.getTransport();
        final String transportName = transport.getName();

        String responseText = null;
        String contentType = null;
        boolean requestProcessed = false;

        // process server configuration request
        if ("http".equalsIgnoreCase(transportName) || "https".equalsIgnoreCase(transportName)) {
            try {
                URI requestUri = new URI(request.getRequestUri()).normalize();

                if (server.getConfigUri().getPath().equals(requestUri.getPath())) {
                    ServerConfiguration config = server.generateServerConfiguration(
                            ((InetSocketAddress) connection.getLocalAddress()).getHostName(),
                            ((InetSocketAddress) connection.getRemoteAddress()).getHostName());

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
            ServiceHandler sh;
            if (serviceHandler == null || transport.isAddressContainsRequestPath()) {
                sh = server.findAcceptingServiceHandler(request.getLocalTransportAddress());
            } else {
                sh = serviceHandler;
            }

            if (sh != null) {
                return sh.performCall(request, response);
            } else {
                logger.error("No service handler for request: {}", request);
                responseText = "No service handler for request";
                contentType = "text/plain; charset=UTF-8";
            }
        }

        try {
            if (responseText != null && contentType != null) {
                response.setPayload(ByteBuffer.wrap(responseText.getBytes("UTF-8")));
                response.setContentType(contentType);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error("No UTF-8 encoding", ex);
        }

        return Futures.immediateFuture(response);
    }

    @Override
    public void close() throws IOException {
        transportConnection.close();
    }
}

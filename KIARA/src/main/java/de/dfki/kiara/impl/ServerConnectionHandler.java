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

import de.dfki.kiara.RequestHandler;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
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
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConnectionHandler implements RequestHandler<TransportMessage, TransportMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ServerConnectionHandler.class);

    private final ServerImpl server;
    private final ServiceHandler serviceHandler;

    public ServerConnectionHandler(ServerImpl server, ServiceHandler serviceHandler) {
        this.server = server;
        this.serviceHandler = serviceHandler;
    }

    public ServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    @Override
    public TransportMessage onRequest(TransportMessage message) {
        final TransportConnection connection = message.getConnection();
        final TransportMessage response = connection.createResponse(message);

        if ("http".equals(message.getConnection().getTransport().getName())) {

            String responseText;
            String contentType;

            try {
                URI requestUri = new URI(message.getRequestUri());

                if (server.getConfigUri().getPath().equals(requestUri.getPath())) {
                    ServerConfiguration config = server.generateServerConfiguration(
                            ((InetSocketAddress) connection.getLocalAddress()).getHostName(),
                            ((InetSocketAddress) connection.getRemoteAddress()).getHostName());

                    responseText = config.toJson();
                    contentType = "application/json";
                } else {
                    ServiceHandler serviceHandler = server.findAcceptingServiceHandler(message.getLocalTransportAddress());
                    if (serviceHandler == null) {
                        responseText = "Unknown service"; // FIXME should be an error response
                        contentType = "text/plain; charset=UTF-8";
                    } else {
                        try {
                            serviceHandler.performCall(message, response);
                            return response;
                        } catch (IOException ex) {
                            responseText = ex.toString();
                            contentType = "text/plain; charset=UTF-8";

                        } catch (IllegalAccessException ex) {
                            responseText = ex.toString();
                            contentType = "text/plain; charset=UTF-8";

                        } catch (IllegalArgumentException ex) {
                            responseText = ex.toString();
                            contentType = "text/plain; charset=UTF-8";
                        }
                    }
                }
            } catch (URISyntaxException ex) {
                responseText = ex.toString();
                contentType = "text/plain; charset=UTF-8";
            } catch (IOException ex) {
                responseText = ex.toString();
                contentType = "text/plain; charset=UTF-8";
            }

            try {
                response.setPayload(ByteBuffer.wrap(responseText.getBytes("UTF-8")));
                response.setContentType(contentType);
            } catch (UnsupportedEncodingException ex) {
                logger.error("No UTF-8 encoding", ex);
            }
        }
        return response;
    }

}

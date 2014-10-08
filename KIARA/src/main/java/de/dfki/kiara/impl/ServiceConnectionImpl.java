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
import de.dfki.kiara.Protocol;
import de.dfki.kiara.ServerConnection;
import de.dfki.kiara.ServiceConnection;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportMessage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServiceConnectionImpl implements ServiceConnection {

    private final ServerConnectionHandler serverConnectionHandler;
    private final TransportAddress transportAddress;
    private final ServiceHandler serviceHandler;

    public ServiceConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddress transportAddress, ServiceHandler serviceHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.transportAddress = transportAddress;
        this.serviceHandler = serviceHandler;
    }

    public ServiceConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddressAndServiceHandler transportAddressAndServiceHandler) {
        this(serverConnectionHandler, transportAddressAndServiceHandler.transportAddress, transportAddressAndServiceHandler.serviceHandler);
    }

    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandler;
    }

    public TransportAddress getTransportAddress() {
        return transportAddress;
    }

    public ServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    @Override
    public ServerConnection getServerConnection() {
        return serverConnectionHandler;
    }
    
    @Override
    public Protocol getProtocol() {
        return serviceHandler.getProtocol();
    }

    public ListenableFuture<TransportMessage> performCall(TransportMessage request, final TransportMessage response) throws IOException, IllegalAccessException, IllegalArgumentException, ExecutionException, InterruptedException {
        return getServiceHandler().performCall(this, request, response);
    }

}

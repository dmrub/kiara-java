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

import java.io.IOException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConnectionImpl implements ServerConnection, InvocationEnvironment {

    private final ServerConnectionHandler serverConnectionHandler;
    private final TransportAddress transportAddress;
    private final ServiceHandler serviceHandler;
    private final InterfaceCodeGen codegen;
    private final ServiceMethodBinding serviceMethodBinding;

    public ServerConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddress transportAddress, ServiceHandler serviceHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.transportAddress = transportAddress;
        this.serviceHandler = serviceHandler;
        this.codegen = serviceHandler.getProtocol().createInterfaceCodeGen(this);
        this.serviceMethodBinding = (ServiceMethodBinding)serviceHandler.getServiceMethodExecutor();
    }

    public ServerConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddressAndServiceHandler transportAddressAndServiceHandler) {
        this(serverConnectionHandler, transportAddressAndServiceHandler.transportAddress, transportAddressAndServiceHandler.serviceHandler);
    }

    @Override
    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandler;
    }

    @Override
    public TransportAddress getTransportAddress() {
        return transportAddress;
    }

    @Override
    public ServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public ServerConnectionHandler getServerConnection() {
        return serverConnectionHandler;
    }

    public ServiceMethodBinding getServiceMethodBinding() {
        return serviceMethodBinding;
    }

    @Override
    public Protocol getProtocol() {
        return serviceHandler.getProtocol();
    }

    @Override
    public ServerConnection getServiceConnection() {
        return this;
    }

    @Override
    public <T> T getServiceInterface(MethodBinding<T> methodBinding) {
        // FIXME compare with ConnectionImpl.getServiceInterface()
        InterfaceMapping<T> mapping = new InterfaceMapping<>(methodBinding);
        Class<T> interfaceClass = mapping.getInterfaceClass();

        return codegen.generateInterfaceImpl(interfaceClass, mapping);
    }

    @Override
    public TransportConnection getTransportConnection() {
        return serverConnectionHandler.getTransportConnection();
    }

    @Override
    public void close() throws IOException {
        // FIXME implement close
    }

    @Override
    public ListenableFuture<Message> performRemoteAsyncCall(Message request, ListeningExecutorService executor) throws IOException {
        return serverConnectionHandler.performRemoteAsyncCall(request, executor);
    }
}

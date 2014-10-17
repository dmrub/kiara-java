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
import de.dfki.kiara.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServiceConnectionImpl implements ServiceConnection, InvocationEnvironment {

    private final ServerConnectionHandler serverConnectionHandler;
    private final TransportAddress transportAddress;
    private final ServiceHandler serviceHandler;
    private final InterfaceCodeGen codegen;

    public ServiceConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddress transportAddress, ServiceHandler serviceHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.transportAddress = transportAddress;
        this.serviceHandler = serviceHandler;
        this.codegen = serviceHandler.getProtocol().createInterfaceCodeGen(this);
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

    @Override
    public ServiceConnection getServiceConnection() {
        return this;
    }

    public ListenableFuture<Message> performCall(final Message requestMessage) throws IOException, IllegalAccessException, IllegalArgumentException, ExecutionException, InterruptedException {
        return getServiceHandler().performCall(this, requestMessage);
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
    public ServiceMethodExecutor getServiceMethodExecutor() {
        return getServiceHandler().getServiceMethodExecutor();
    }

    @Override
    public void close() throws IOException {
        // FIXME implement close
    }

    @Override
    public MessageConnection getMessageConnection() {
        return serverConnectionHandler;
    }
}

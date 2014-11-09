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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConnectionImpl implements ServerConnection, InvocationEnvironment {

    private final ServerConnectionHandler serverConnectionHandler;
    private final TransportAddress transportAddress;
    private final ServiceHandler serviceHandler;
    private final InterfaceCodeGen codegen;
    private final ServiceMethodExecutor serviceMethodExecutor;
    private final Map<Class<?>, Object> instanceCache;

    public ServerConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddress transportAddress, ServiceHandler serviceHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.transportAddress = transportAddress;
        this.serviceHandler = serviceHandler;
        this.codegen = serviceHandler.getProtocol().createInterfaceCodeGen(this);
        this.serviceMethodExecutor = serviceHandler.getServiceMethodExecutor();
        this.instanceCache = new HashMap<>();
    }

    public ServerConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddressAndServiceHandler transportAddressAndServiceHandler) {
        this(serverConnectionHandler, transportAddressAndServiceHandler.transportAddress, transportAddressAndServiceHandler.serviceHandler);
    }

    @Override
    public TransportAddress getTransportAddress() {
        return transportAddress;
    }

    public ServerConnectionHandler getServerConnection() {
        return serverConnectionHandler;
    }

    public de.dfki.kiara.ServiceMethodExecutor getServiceMethodExecutor() {
        return serviceMethodExecutor;
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
        final InterfaceMapping<T> mapping = new InterfaceMapping<>(methodBinding);
        final Class<T> interfaceClass = mapping.getInterfaceClass();

        final T result = codegen.generateInterfaceImpl(interfaceClass, mapping);

        synchronized (instanceCache) {
            instanceCache.put(interfaceClass, result);
        }

        return result;
    }

    @Override
    public <T> T getServiceInterface(Class<T> interfaceClass) {
        synchronized (instanceCache) {
            Object instance = instanceCache.get(interfaceClass);
            if (instance == null) {
                return null;
            }
            return (T) instance;
        }
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

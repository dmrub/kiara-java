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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.*;
import de.dfki.kiara.util.Pipeline;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConnectionImpl implements ServerConnection, InvocationEnvironment {

    private static final Logger logger = LoggerFactory.getLogger(ServerConnectionImpl.class);
    private final ServerConnectionHandler serverConnectionHandler;
    private final TransportAddress transportAddress;
    private final ServiceHandler serviceHandler;
    private final InterfaceCodeGen codegen;

    public ServerConnectionImpl(ServerConnectionHandler serverConnectionHandler, TransportAddress transportAddress, ServiceHandler serviceHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.transportAddress = transportAddress;
        this.serviceHandler = serviceHandler;
        this.codegen = serviceHandler.getProtocol().createInterfaceCodeGen(this);

        final ServiceMethodBinding serviceMethodBinding = (ServiceMethodBinding)serviceHandler.getServiceMethodExecutor();
        final Pipeline pipeline = serverConnectionHandler.getPipeline();
        final MessageConnection messageConnection = serverConnectionHandler;
        serverConnectionHandler.addMessageListener(new MessageListener() {

            @Override
            public void onMessage(MessageConnection connection, Message message) {
                if (message == null) {
                    logger.error("Received null message");
                    return;
                }

                try {

                    logger.info("Incoming message: {}", message);

                    if (message.getMessageKind() == Message.Kind.REQUEST) {
                        // FIXME compare with ServerConnectionHandler.onRequest

                        ListenableFuture<Message> fmsg = serviceMethodBinding.performCall(null, message);

                        Futures.addCallback(fmsg, new FutureCallback<Message>() {

                            @Override
                            public void onSuccess(Message resultMessage) {
                                try {
                                    messageConnection.send(resultMessage);
                                } catch (Exception ex) {
                                    logger.error("Error on callback response", ex);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                logger.error("Error on callback response", t);
                            }
                        }, Global.executor);

                        return;
                    }

                    // process via pipeline
                    Object processResult = pipeline.process(message);
                    if (processResult != null) {
                        logger.warn("Unprocessed transport message: {}: {}", processResult.getClass(), processResult);
                    }
                } catch (Exception ex) {
                    logger.error("Pipeline processing failed", ex);
                }
            }
        });
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

    @Override
    public Protocol getProtocol() {
        return serviceHandler.getProtocol();
    }

    @Override
    public ServerConnection getServiceConnection() {
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

    @Override
    public Pipeline getMessagePipeline() {
        return serverConnectionHandler.getPipeline();
    }
}

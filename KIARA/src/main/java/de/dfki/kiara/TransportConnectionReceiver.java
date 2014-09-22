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
package de.dfki.kiara;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportConnectionReceiver implements Handler<TransportMessage>, TransportConnection {

    private final TransportConnection connection;
    private final BlockingQueue<Object> queue = new LinkedBlockingDeque<>();
    private static final ListeningExecutorService sameThreadExecutor = MoreExecutors.sameThreadExecutor();

    public TransportConnectionReceiver(TransportConnection connection) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        this.connection = connection;
        this.connection.addResponseHandler(this);
    }

    public void detach() {
        this.connection.removeResponseHandler(this);
    }

    public TransportConnection getConnection() {
        return connection;
    }

    @Override
    public SocketAddress getLocalAddress() {
        return connection.getLocalAddress();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return connection.getRemoteAddress();
    }

    @Override
    public TransportMessage createRequest() {
        return connection.createRequest();
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        return connection.send(message);
    }

    public ListenableFuture<TransportMessage> receive(ListeningExecutorService executor) {
        if (executor == null) {
            executor = sameThreadExecutor;
        }
        return executor.submit(new Callable<TransportMessage>() {

            @Override
            public TransportMessage call() throws Exception {
                boolean interrupted = false;
                try {
                    for (;;) {
                        try {
                            Object value = queue.take();
                            if (value instanceof Exception) {
                                throw (Exception) value;
                            }
                            return (TransportMessage) value;
                        } catch (InterruptedException ignore) {
                            interrupted = true;
                        }
                    }
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    @Override
    public boolean onSuccess(TransportMessage result) {
        queue.add(result);
        return true;
    }

    @Override
    public boolean onFailure(Throwable t) {
        queue.add(t);
        return true;
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

    @Override
    public void addResponseHandler(Handler<TransportMessage> handler) {
        connection.addResponseHandler(handler);
    }

    @Override
    public boolean removeResponseHandler(Handler<TransportMessage> handler) {
        return connection.removeResponseHandler(handler);
    }

    @Override
    public void addRequestHandler(RequestHandler<TransportMessage, ListenableFuture<TransportMessage>> handler) {
        connection.addRequestHandler(handler);
    }

    @Override
    public void removeRequestHandler(RequestHandler<TransportMessage, ListenableFuture<TransportMessage>> handler) {
        connection.removeRequestHandler(handler);
    }

    @Override
    public TransportMessage createResponse(TransportMessage request) {
        return connection.createResponse(request);
    }

    @Override
    public TransportAddress getLocalTransportAddress() {
        return connection.getLocalTransportAddress();
    }

    @Override
    public Transport getTransport() {
        return connection.getTransport();
    }
}

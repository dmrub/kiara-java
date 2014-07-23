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
package de.dfki.kiara.jsonrpc;

import com.google.common.base.Function;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.dfki.kiara.Connection;
import de.dfki.kiara.Handler;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Message;
import de.dfki.kiara.RunningService;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportConnectionReceiver;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.Util;
import de.dfki.kiara.WrappedRemoteException;
import de.dfki.kiara.impl.SpecialMethods;
import de.dfki.kiara.util.Pipeline;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcInvocationHandler extends AbstractInvocationHandler implements Handler<TransportMessage>, RunningService {

    private final Connection connection;
    private final InterfaceMapping<?> interfaceMapping;
    private final JsonRpcProtocol protocol;
    //private static final ListeningExecutorService executor = MoreExecutors.sameThreadExecutor();
    private static final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    private static final ListeningExecutorService sameThreadExecutor = MoreExecutors.sameThreadExecutor();
    private static final Logger logger = LoggerFactory.getLogger(JsonRpcInvocationHandler.class);

    private final Pipeline pipeline;

    public JsonRpcInvocationHandler(Connection connection, InterfaceMapping<?> interfaceMapping, JsonRpcProtocol protocol) {
        this.connection = connection;
        this.interfaceMapping = interfaceMapping;
        this.protocol = protocol;
        this.pipeline = new Pipeline();
        this.pipeline.addHandler(new JsonRpcMessageDecoder(protocol));
        this.connection.getTransportConnection().addResponseHandler(this);
        Kiara.addRunningService(this);
    }

    public InterfaceMapping<?> getInterfaceMapping() {
        return interfaceMapping;
    }

    public Connection getConnection() {
        return connection;
    }

    public static ListenableFuture<TransportMessage> performAsyncCall(TransportMessage request, final ListeningExecutorService executor) {
        final TransportConnectionReceiver connection = new TransportConnectionReceiver(request.getConnection());
        ListenableFuture<Void> reqSent = connection.send(request);
        AsyncFunction<Void, TransportMessage> f = new AsyncFunction<Void, TransportMessage>() {

            @Override
            public ListenableFuture<TransportMessage> apply(Void input) throws Exception {
                return connection.receive(executor);
            }
        };
        return Futures.transform(reqSent, f);
    }

    public ListenableFuture<Message> performAsyncCall(Message request, final Method method, final ListeningExecutorService executor) throws IOException {
        final TransportConnection tc = connection.getTransportConnection();
        final TransportMessage transportRequest = tc.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());

        ListenableFuture<TransportMessage> transportResponse = performAsyncCall(transportRequest, executor);
        Function<TransportMessage, Message> f = new Function<TransportMessage, Message>() {

            @Override
            public Message apply(TransportMessage input) {
                try {
                    return protocol.createResponseMessageFromData(input.getPayload());
                } catch (IOException ex) {
                    return null;
                }
            }
        };

        return Futures.transform(transportResponse, f);
    }

    public Message performSyncCall(Message request, Method method) throws InterruptedException, ExecutionException, IOException {
        final TransportConnectionReceiver tc = new TransportConnectionReceiver(connection.getTransportConnection());
        final TransportMessage transportRequest = tc.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());

        // send & wait
        tc.send(transportRequest).get();
        // receive
        ListenableFuture<TransportMessage> responseFuture = tc.receive(null);
        TransportMessage transportResponse = responseFuture.get();

        return protocol.createResponseMessageFromData(transportResponse.getPayload());
    }

    public ListenableFuture<Message> performAsyncCall2(final Message request, final Class<?> returnType, ListeningExecutorService executor) throws IOException {
        final TransportConnection tc = connection.getTransportConnection();
        final TransportMessage transportRequest = tc.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());
        final JsonRpcMessageDispatcher dispatcher = new JsonRpcMessageDispatcher(protocol, ((JsonRpcMessage) request).getId());
        pipeline.addHandler(dispatcher);

        ListenableFuture<Void> reqSent = tc.send(transportRequest);

        final ListeningExecutorService myExecutor = executor == null ? sameThreadExecutor : executor;

        AsyncFunction<Void, Message> f = new AsyncFunction<Void, Message>() {

            @Override
            public ListenableFuture<Message> apply(Void input) throws Exception {
                return myExecutor.submit(new Callable<Message>() {

                    @Override
                    public Message call() throws Exception {
                        boolean interrupted = false;
                        try {
                            for (; ; ) {
                                try {
                                    Object value = dispatcher.getQueue().take();
                                    if (value instanceof Exception) {
                                        throw (Exception) value;
                                    }
                                    return (Message) value;
                                } catch (InterruptedException ignore) {
                                    interrupted = true;
                                } finally {
                                    pipeline.removeHandler(dispatcher);
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
        };
        return Futures.transform(reqSent, f);
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] os) throws Throwable {
        if (method.equals(SpecialMethods.riGetConnection)) {
            return getConnection();
        }

        InterfaceMapping<?> mapping = getInterfaceMapping();

        final String idlMethodName = mapping.getIDLMethodName(method);
        if (idlMethodName == null) {
            throw new UnsupportedOperationException("Unbound method: " + method);
        }

        // check for Future<?>
        final InterfaceMapping<?>.MethodEntry methodEntry = mapping.getMethodEntry(method);

        logger.debug("has future params {} has listeningfuture params {}", methodEntry.hasFutureParams, methodEntry.hasListeningFutureParams);

        ListenableFuture<List<Object>> futureParams = null;

        if (methodEntry.hasFutureParams) {
            List<ListenableFuture<Object>> futureParamsList = new ArrayList<>(os.length);
            for (int i = 0; i < os.length; ++i) {
                final Function<Object, Object> f = ((Function<Object, Object>) methodEntry.paramConverters[i]);
                futureParamsList.add((ListenableFuture<Object>) f.apply(os[i]));
            }
            futureParams = Futures.allAsList(futureParamsList);
            //System.out.format("futureParams = %s%n", Joiner.on(" ").join(futureParams.get()));
        }

        if (Util.isSerializer(method)) {
            if (futureParams != null) {
                return protocol.createRequestMessage(new Message.RequestObject(idlMethodName, futureParams.get()));
            } else {
                return protocol.createRequestMessage(new Message.RequestObject(idlMethodName, os));
            }
        } else if (Util.isDeserializer(method)) {
            Message msg = (Message) os[0];
            Message.ResponseObject ro = msg.getResponseObject(method.getReturnType());

            if (ro.isException) {
                if (ro.result instanceof Exception) {
                    throw (Exception) ro.result;
                }
                throw new WrappedRemoteException(ro.result);
            }

            return ro.result;
        } else {
            if (futureParams != null && methodEntry.futureParamOfReturnType != null) {

                AsyncFunction<List<Object>, Object> f = new AsyncFunction<List<Object>, Object>() {

                    @Override
                    public ListenableFuture<Object> apply(List<Object> params) throws Exception {
                        final JsonRpcMessage request = (JsonRpcMessage) protocol.createRequestMessage(new Message.RequestObject(idlMethodName, params));
                        final Class<?> returnType = Util.toClass(methodEntry.futureParamOfReturnType);
                        final ListenableFuture<Message> responseFuture = performAsyncCall2(request, returnType, executor);
                        AsyncFunction<Message, Object> g = new AsyncFunction<Message, Object>() {

                            @Override
                            public ListenableFuture<Object> apply(final Message response) throws Exception {
                                return executor.submit(new Callable<Object>() {

                                    @Override
                                    public Object call() throws Exception {
                                        Message.ResponseObject ro = response.getResponseObject(returnType);

                                        if (ro.isException) {
                                            if (ro.result instanceof Exception) {
                                                throw (Exception) ro.result;
                                            }
                                            throw new WrappedRemoteException(ro.result);
                                        }

                                        return ro.result;

                                    }
                                });
                            }
                        };
                        return Futures.transform(responseFuture, g);
                    }
                };
                return Futures.transform(futureParams, f);
            } else {

                List<Object> params = futureParams != null ? futureParams.get() : Arrays.asList(os);

                final JsonRpcMessage request = (JsonRpcMessage) protocol.createRequestMessage(new Message.RequestObject(idlMethodName, params));

                final Class<?> returnType = methodEntry.futureParamOfReturnType != null ? Util.toClass(methodEntry.futureParamOfReturnType) : method.getReturnType();

                final ListenableFuture<Message> responseFuture = performAsyncCall2(request, returnType, executor);

                if (methodEntry.futureParamOfReturnType != null) {
                    AsyncFunction<Message, Object> f = new AsyncFunction<Message, Object>() {

                        @Override
                        public ListenableFuture<Object> apply(final Message response) throws Exception {
                            return executor.submit(new Callable<Object>() {

                                @Override
                                public Object call() throws Exception {
                                    Message.ResponseObject ro = response.getResponseObject(returnType);

                                    if (ro.isException) {
                                        if (ro.result instanceof Exception) {
                                            throw (Exception) ro.result;
                                        }
                                        throw new WrappedRemoteException(ro.result);
                                    }

                                    return ro.result;

                                }
                            });
                        }
                    };
                    return Futures.transform(responseFuture, f);

                } else {
                    Message response = responseFuture.get();
                    Message.ResponseObject ro = response.getResponseObject(returnType);

                    if (ro.isException) {
                        if (ro.result instanceof Exception) {
                            throw (Exception) ro.result;
                        }
                        throw new WrappedRemoteException(ro.result);
                    }

                    return ro.result;
                }
            }
        }

    }

    @Override
    public boolean onSuccess(TransportMessage result) {
        try {
            Object processResult = pipeline.process(result);
            if (processResult != null) {
                logger.warn("Unprocessed JSON-RPC transport message: {}: {}", processResult.getClass(), processResult);
            }
        } catch (Exception ex) {
            logger.error("Pipeline processing failed: {}", ex);
        }
        return true;

    }

    @Override
    public boolean onFailure(Throwable t) {
        try {
            pipeline.process(t);
            return true;
        } catch (Exception ex) {
            logger.error("Pipeline processing failed: {}", ex);
        }
        return true;
    }

    @Override
    public void shutdownGracefully() {
        executor.shutdown();
    }

}

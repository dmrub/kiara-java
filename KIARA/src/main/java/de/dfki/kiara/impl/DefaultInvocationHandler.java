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

import com.google.common.base.Function;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.util.concurrent.*;
import de.dfki.kiara.*;
import de.dfki.kiara.util.MessageDecoder;
import de.dfki.kiara.util.MessageDispatcher;
import de.dfki.kiara.util.Pipeline;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dmitri Rubinstein on 30.07.2014.
 * @param <PROTOCOL>
 */
public abstract class DefaultInvocationHandler<PROTOCOL extends Protocol> extends AbstractInvocationHandler implements Handler<TransportMessage> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInvocationHandler.class);
    protected final Connection connection;
    protected final InterfaceMapping<?> interfaceMapping;
    protected final PROTOCOL protocol;
    protected final Pipeline pipeline;

    public DefaultInvocationHandler(Connection connection, InterfaceMapping<?> interfaceMapping, PROTOCOL protocol, MessageDecoder<PROTOCOL> messageDecoder) {
        this.connection = connection;
        this.pipeline = new Pipeline();
        this.protocol = protocol;
        this.interfaceMapping = interfaceMapping;
        this.pipeline.addHandler(messageDecoder);
        this.connection.getTransportConnection().addResponseHandler(this);
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

    public InterfaceMapping<?> getInterfaceMapping() {
        return interfaceMapping;
    }

    public Connection getConnection() {
        return connection;
    }

    @Deprecated
    protected ListenableFuture<Message> performAsyncCallOld(Message request, final Method method, final ListeningExecutorService executor) throws IOException {
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

    protected Message performSyncCall(Message request, Method method) throws InterruptedException, ExecutionException, IOException {
        final TransportConnection tc = connection.getTransportConnection();
        tc.removeResponseHandler(this);
        final TransportConnectionReceiver tcr = new TransportConnectionReceiver(tc);

        final TransportMessage transportRequest = tcr.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());

        // send & wait
        tcr.send(transportRequest).get();
        // receive
        ListenableFuture<TransportMessage> responseFuture = tcr.receive(null);
        TransportMessage transportResponse = responseFuture.get();

        tcr.detach();

        tc.addResponseHandler(this);

        return protocol.createResponseMessageFromData(transportResponse.getPayload());
    }

    public abstract MessageDispatcher createMessageDispatcher(Message request);

    protected ListenableFuture<Message> performAsyncCall(final Message request, final Class<?> returnType, ListeningExecutorService executor) throws IOException {
        final TransportConnection tc = connection.getTransportConnection();
        final TransportMessage transportRequest = tc.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());
        final MessageDispatcher dispatcher = createMessageDispatcher(request);
        pipeline.addHandler(dispatcher);

        ListenableFuture<Void> reqSent = tc.send(transportRequest);

        final ListeningExecutorService myExecutor = executor == null ? Global.sameThreadExecutor : executor;

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
        final MethodEntry methodEntry = mapping.getMethodEntry(method);

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

        if (methodEntry.kind == MethodEntry.MethodKind.SERIALIZER) {
            if (futureParams != null) {
                return protocol.createRequestMessage(new Message.RequestObject(idlMethodName, futureParams.get()));
            } else {
                return protocol.createRequestMessage(new Message.RequestObject(idlMethodName, os));
            }
        } else if (methodEntry.kind == MethodEntry.MethodKind.DESERIALIZER) {
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
                        final Message request = protocol.createRequestMessage(new Message.RequestObject(idlMethodName, params));
                        final Class<?> returnType = Util.toClass(methodEntry.futureParamOfReturnType);
                        final ListenableFuture<Message> responseFuture = performAsyncCall(request, returnType, Global.executor);
                        AsyncFunction<Message, Object> g = new AsyncFunction<Message, Object>() {

                            @Override
                            public ListenableFuture<Object> apply(final Message response) throws Exception {
                                return Global.executor.submit(new Callable<Object>() {

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

                /* Following code is for testing of synchronous message sending

                if (futureParams == null && methodEntry.futureParamOfReturnType == null) {
                    final Message request = protocol.createRequestMessage(new Message.RequestObject(idlMethodName, os));
                    final Message response = performSyncCall(request, method);
                    final Message.ResponseObject ro = response.getResponseObject(method.getReturnType());
                    if (ro.isException) {
                        if (ro.result instanceof Exception) {
                            throw (Exception) ro.result;
                        }
                        throw new WrappedRemoteException(ro.result);
                    }

                    return ro.result;
                }
                */

                List<Object> params = futureParams != null ? futureParams.get() : Arrays.asList(os);

                final Message request = protocol.createRequestMessage(new Message.RequestObject(idlMethodName, params));

                final Class<?> returnType = methodEntry.futureParamOfReturnType != null ? Util.toClass(methodEntry.futureParamOfReturnType) : method.getReturnType();

                final ListenableFuture<Message> responseFuture = performAsyncCall(request, returnType, Global.executor);

                if (methodEntry.futureParamOfReturnType != null) {
                    AsyncFunction<Message, Object> f = new AsyncFunction<Message, Object>() {

                        @Override
                        public ListenableFuture<Object> apply(final Message response) throws Exception {
                            return Global.executor.submit(new Callable<Object>() {

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
                logger.warn("Unprocessed transport message: {}: {}", processResult.getClass(), processResult);
            }
        } catch (Exception ex) {
            logger.error("Pipeline processing failed", ex);
        }
        return true;

    }

    @Override
    public boolean onFailure(Throwable t) {
        try {
            pipeline.process(t);
            return true;
        } catch (Exception ex) {
            logger.error("Pipeline processing failed", ex);
        }
        return true;
    }

}

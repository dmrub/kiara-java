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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import de.dfki.kiara.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import de.dfki.kiara.util.MessageDispatcher;
import de.dfki.kiara.util.Pipeline;

/**
 * Created by Dmitri Rubinstein on 30.07.2014.
 *
 * @param <PROTOCOL>
 */
public abstract class DefaultInvocationHandler<PROTOCOL extends Protocol> extends AbstractInvocationHandler implements Handler<TransportMessage> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultInvocationHandler.class);
    protected final ConnectionBase connection;
    protected final TransportConnection transportConnection;
    protected final InterfaceMapping<?> interfaceMapping;
    protected final ServiceMethodBinding serviceMethodBinding;
    protected final PROTOCOL protocol;
    protected final Pipeline pipeline;

    public DefaultInvocationHandler(ConnectionBase connection, TransportConnection transportConnection, InterfaceMapping<?> interfaceMapping, ServiceMethodBinding serviceMethodBinding, PROTOCOL protocol) {
        this.connection = connection;
        this.transportConnection = transportConnection;
        this.pipeline = new Pipeline();
        this.protocol = protocol;
        this.interfaceMapping = interfaceMapping;
        this.serviceMethodBinding = serviceMethodBinding;
        this.transportConnection.addResponseHandler(this);
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

    public TransportConnection getTransportConnection() {
        return transportConnection;
    }

    @Deprecated
    protected ListenableFuture<Message> performAsyncCallOld(Message request, final Method method, final ListeningExecutorService executor) throws IOException {
        final TransportMessage transportRequest = transportConnection.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());

        ListenableFuture<TransportMessage> transportResponse = performAsyncCall(transportRequest, executor);
        Function<TransportMessage, Message> f = new Function<TransportMessage, Message>() {

            @Override
            public Message apply(TransportMessage input) {
                try {
                    return protocol.createMessageFromData(input.getPayload());
                } catch (IOException ex) {
                    return null;
                }
            }
        };

        return Futures.transform(transportResponse, f);
    }

    protected Message performSyncCall(Message request, Method method) throws InterruptedException, ExecutionException, IOException {
        transportConnection.removeResponseHandler(this);
        final TransportConnectionReceiver tcr = new TransportConnectionReceiver(transportConnection);

        final TransportMessage transportRequest = tcr.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());

        // send & wait
        tcr.send(transportRequest).get();
        // receive
        ListenableFuture<TransportMessage> responseFuture = tcr.receive(null);
        TransportMessage transportResponse = responseFuture.get();

        tcr.detach();

        transportConnection.addResponseHandler(this);

        return protocol.createMessageFromData(transportResponse.getPayload());
    }

    public abstract MessageDispatcher createMessageDispatcher(Message request);

    protected ListenableFuture<Message> performAsyncCall(final Message request, final TypeToken<?> returnType, ListeningExecutorService executor) throws IOException {
        final TransportMessage transportRequest = transportConnection.createRequest();
        transportRequest.setContentType(protocol.getMimeType());
        transportRequest.setPayload(request.getMessageData());
        final MessageDispatcher dispatcher = createMessageDispatcher(request);
        pipeline.addHandler(dispatcher);

        final ListenableFuture<Void> reqSent = transportConnection.send(transportRequest);

        final ListeningExecutorService myExecutor = executor == null ? Global.sameThreadExecutor : executor;

        AsyncFunction<Void, Message> f = new AsyncFunction<Void, Message>() {

            @Override
            public ListenableFuture<Message> apply(Void input) throws Exception {
                return myExecutor.submit(new Callable<Message>() {

                    @Override
                    public Message call() throws Exception {
                        boolean interrupted = false;
                        try {
                            for (;;) {
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
            return connection;
        }

        InterfaceMapping<?> mapping = getInterfaceMapping();

        final String idlFunctionName = mapping.getIDLMethodName(method);
        if (idlFunctionName == null) {
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
                return protocol.createRequestMessage(new Message.RequestObject(idlFunctionName, futureParams.get()));
            } else {
                return protocol.createRequestMessage(new Message.RequestObject(idlFunctionName, os));
            }
        } else if (methodEntry.kind == MethodEntry.MethodKind.DESERIALIZER) {
            Message msg = (Message) os[0];
            Message.ResponseObject ro = msg.getResponseObject(TypeToken.of(method.getGenericReturnType()));

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
                        final Message request = protocol.createRequestMessage(new Message.RequestObject(idlFunctionName, params));
                        final TypeToken<?> returnType = TypeToken.of(methodEntry.futureParamOfReturnType);
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
                 final Message request = protocol.createRequestMessage(new Message.RequestObject(idlFunctionName, os));
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

                final Message request = protocol.createRequestMessage(new Message.RequestObject(idlFunctionName, params));

                final TypeToken<?> returnType = methodEntry.futureParamOfReturnType != null ? TypeToken.of(methodEntry.futureParamOfReturnType) : TypeToken.of(method.getGenericReturnType());

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

                    Message response;
                    try {
                        response = responseFuture.get();
                    } catch (Exception ex) {
                        throw new RemoteInvocationException(ex);
                    }
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
    public boolean onSuccess(final TransportMessage tmessage) {
        if (tmessage == null) {
            logger.error("Received null transport message");
            return true;
        }

        try {
            Message message = protocol.createMessageFromData(tmessage.getPayload());

            if (message.getMessageKind() == Message.Kind.REQUEST) {
                // FIXME compare with ServerConnectionHandler.onRequest

                ListenableFuture<Message> fmsg = serviceMethodBinding.performCall(null, message);

                Futures.addCallback(fmsg, new FutureCallback<Message>() {

                    @Override
                    public void onSuccess(Message resultMessage) {
                        try {
                            final TransportConnection tc = tmessage.getConnection();
                            final TransportMessage tresponse = tc.createResponse(tmessage);
                            tresponse.setPayload(resultMessage.getMessageData());
                            tresponse.setContentType(protocol.getMimeType());
                            tc.send(tresponse);
                        } catch (Exception ex) {
                            logger.error("Error on callback response", ex);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        logger.error("Error on callback response", t);
                    }
                }, Global.executor);

                return true;
            }

            // process via pipeline
            Object processResult = pipeline.process(message);
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

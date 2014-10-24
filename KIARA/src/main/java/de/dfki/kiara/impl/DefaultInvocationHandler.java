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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import de.dfki.kiara.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Created by Dmitri Rubinstein on 30.07.2014.
 *
 */
public class DefaultInvocationHandler extends AbstractInvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultInvocationHandler.class);
    protected final ConnectionBase connection;
    protected final InterfaceMapping<?> interfaceMapping;
    protected final Protocol protocol;

    public DefaultInvocationHandler(ConnectionBase connection, InterfaceMapping<?> interfaceMapping, Protocol protocol) {
        this.connection = connection;
        this.protocol = protocol;
        this.interfaceMapping = interfaceMapping;
    }

    public InterfaceMapping<?> getInterfaceMapping() {
        return interfaceMapping;
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
                        final ListenableFuture<Message> responseFuture = connection.performRemoteAsyncCall(request, Global.executor);
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

                final ListenableFuture<Message> responseFuture = connection.performRemoteAsyncCall(request, Global.executor);

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

}

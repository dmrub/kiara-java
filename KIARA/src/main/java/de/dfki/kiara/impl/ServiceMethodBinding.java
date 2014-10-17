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

import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Message;
import de.dfki.kiara.MethodEntry;
import de.dfki.kiara.NoSuchIDLFunctionException;
import de.dfki.kiara.Protocol;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.dfki.kiara.ServiceMethodBinder;
import de.dfki.kiara.ServiceMethodExecutor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author shahzad
 */
public class ServiceMethodBinding implements ServiceMethodExecutor {

    private final Map<String, ServiceMethodBinder> internalMapping;

    public ServiceMethodBinding() {
        internalMapping = new HashMap<>();
    }

    public void bindServiceMethod(String idlFunctionName, Object serviceClass,
                                  String serviceMethodName) throws NoSuchMethodException, SecurityException {
        Method method = null;
        for (Method m : serviceClass.getClass().getMethods()) {
            if (m.getName().equals(serviceMethodName)) {
                method = m;
                break;
            }
        }

        if (method == null)
            throw new IllegalArgumentException("No such method "+serviceMethodName+" in class "+serviceClass.getClass().getName());

        internalMapping.put(idlFunctionName, new ServiceMethodBinder(serviceClass, method));
    }

    public void bindServiceMethod(String idlFunctionName, Object serviceClass,
                                  String serviceMethodName, Class<?>[] parameterTypes) throws
            NoSuchMethodException, SecurityException {
        Method method = serviceClass.getClass().getMethod(serviceMethodName, parameterTypes);
        if (method == null)
            throw new IllegalArgumentException("No such method "+serviceMethodName+" with parameter types "+parameterTypes+" in class "+serviceClass.getClass().getName());
        internalMapping.put(idlFunctionName, new ServiceMethodBinder(serviceClass, method));
    }

    public ServiceMethodBinder getServiceMethodBinder(String idlFunctionName) {
        return internalMapping.get(idlFunctionName);
    }

    public void unbindServiceMethod(String idlFunctionName) throws NoSuchIDLFunctionException {
        if (internalMapping.containsKey(idlFunctionName)) {
            internalMapping.remove(idlFunctionName);
        } else {
            throw new NoSuchIDLFunctionException(idlFunctionName+" is not bound");
        }
    }

    public ListenableFuture<Message> performCall(InvocationEnvironment env, final Message requestMessage) throws IOException, IllegalAccessException, IllegalArgumentException, ExecutionException, InterruptedException {
        if (requestMessage.getMessageKind() != Message.Kind.REQUEST) {
            throw new IllegalArgumentException("message is not a request");
        }
        final String methodName = requestMessage.getMethodName();
        final Protocol protocol = requestMessage.getProtocol();
        final ServiceMethodBinder serviceMethodBinder = getServiceMethodBinder(methodName);

        Object result;
        boolean isException = false;

        if (serviceMethodBinder == null) {
            isException = true;
            result = new GenericRemoteException("unbound method '"+methodName+"'", GenericRemoteException.METHOD_NOT_FOUND);

            Message responseMessage = protocol.createResponseMessage(requestMessage, new Message.ResponseObject(result, isException));
            return Futures.immediateFuture(responseMessage);
        } else {
            final MethodEntry methodEntry = serviceMethodBinder.getMethodEntry();

            final List<Object> args = requestMessage.getRequestObject(methodEntry.serializationParamTypes).args;

            //methodEntry.hasFutureParams
            final int numArgs = args.size();
            for (int i = 0; i < numArgs; ++i) {
                if (methodEntry.isFutureParam.get(i)) {
                    final Function<Object, Object> f = ((Function<Object, Object>) methodEntry.serializationToParamConverters[i]);
                    args.set(i, f.apply(args.get(i)));
                } else if (methodEntry.specialParamTypes[i] != null && env != null) {
                    final TypeToken<?> ptype = methodEntry.specialParamTypes[i];
                    if (ptype.isAssignableFrom(de.dfki.kiara.ServiceConnection.class)) {
                        args.set(i, env.getServiceConnection());
                    } else if (ptype.isAssignableFrom(de.dfki.kiara.ServerConnection.class)) {
                        args.set(i, env.getServerConnection());
                    }
                }
            }

            AsyncFunction<List<Object>, Message> ff = new AsyncFunction<List<Object>, Message>() {

                @Override
                public ListenableFuture<Message> apply(final List<Object> input) throws Exception {
                    return Global.executor.submit(new Callable<Message>() {
                        @Override
                        public Message call() throws Exception {
                            Object result;
                            boolean isException = false;

                            try {
                                result = serviceMethodBinder.getBoundMethod().invoke(serviceMethodBinder.getImplementedClass(), args.toArray());

                                if (methodEntry.futureParamOfReturnType != null) {
                                    ListenableFuture<?> futureResult =
                                            (ListenableFuture<?>)(methodEntry.returnTypeConverter != null ?
                                                    ((Function<Object, Object>)(methodEntry.returnTypeConverter)).apply(result) :
                                                    result);
                                    result = futureResult.get();
                                }

                            } catch (InvocationTargetException ex) {
                                isException = true;
                                result = ex.getTargetException();
                            }

                            Message responseMessage = protocol.createResponseMessage(requestMessage, new Message.ResponseObject(result, isException));
                            return responseMessage;
                        }
                    });
                }
            };
            return Futures.transform(Futures.immediateFuture(args), ff);
        }
    }

}

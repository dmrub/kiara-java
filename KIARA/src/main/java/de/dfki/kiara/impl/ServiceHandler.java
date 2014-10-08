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

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Message;
import de.dfki.kiara.MethodEntry;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.ProtocolRegistry;
import de.dfki.kiara.Service;
import de.dfki.kiara.ServiceMethodBinder;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.config.ProtocolInfo;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServiceHandler implements Closeable {
    private final ServiceImpl service;
    private final ProtocolInfo protocolInfo;
    private final Protocol protocol;

    public ServiceHandler(ServiceImpl service, Transport transport, String protocolName) throws InstantiationException, IllegalAccessException {
        this.service = service;
        this.protocolInfo = new ProtocolInfo();
        this.protocolInfo.name = protocolName;
        this.protocol = ProtocolRegistry.newProtocolByName(protocolName);
    }

    public Service getService() {
        return service;
    }

    public ProtocolInfo getProtocolInfo() {
        return protocolInfo;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public void close() {
    }

    ListenableFuture<TransportMessage> performCall(ServiceConnectionImpl serviceConnection, TransportMessage request, final TransportMessage response) throws IOException, IllegalAccessException, IllegalArgumentException, ExecutionException, InterruptedException {
        /*
        byte[] array;
        int arrayOffset;
        int arrayLength;
        if (request.getPayload().hasArray()) {
            array = request.getPayload().array();
            arrayOffset = request.getPayload().arrayOffset();
            arrayLength = request.getPayloadSize();
        } else {
            array = new byte[request.getPayloadSize()];
            request.getPayload().get(array);
            arrayOffset = 0;
            arrayLength = 0;
        }
        */

        final Message requestMessage = protocol.createRequestMessageFromData(request.getPayload());
        final String methodName = requestMessage.getMethodName();
        final ServiceMethodBinder serviceMethodBinder = service.getMethodBinding().getServiceMethodBinder(methodName);

        Object result;
        boolean isException = false;

        if (serviceMethodBinder == null) {
            isException = true;
            result = new GenericRemoteException("unbound method '"+methodName+"'", GenericRemoteException.METHOD_NOT_FOUND);
        } else {
            final MethodEntry methodEntry = serviceMethodBinder.getMethodEntry();

            final List<Object> args = requestMessage.getRequestObject(methodEntry.serializationParamTypes).args;

            //methodEntry.hasFutureParams
            final int numArgs = args.size();
            for (int i = 0; i < numArgs; ++i) {
                if (methodEntry.isFutureParam.get(i)) {
                    final Function<Object, Object> f = ((Function<Object, Object>) methodEntry.serializationToParamConverters[i]);
                    args.set(i, f.apply(args.get(i)));
                } else if (methodEntry.specialParamTypes[i] != null) {
                    final TypeToken<?> ptype = methodEntry.specialParamTypes[i];
                    if (ptype.isAssignableFrom(de.dfki.kiara.ServiceConnection.class)) {
                        args.set(i, serviceConnection);
                    } else if (ptype.isAssignableFrom(de.dfki.kiara.ServerConnection.class)) {
                        args.set(i, serviceConnection.getServerConnection());
                    }
                }
            }

            try {
                result = serviceMethodBinder.getBoundMethod().invoke(serviceMethodBinder.getImplementedClass(), args.toArray());
            } catch (InvocationTargetException ex) {
                isException = true;
                result = ex.getTargetException();
            }

            if (methodEntry.futureParamOfReturnType != null) {
                ListenableFuture<Object> futureResult =
                        (ListenableFuture<Object>)(methodEntry.returnTypeConverter != null ?
                                ((Function<Object, Object>)(methodEntry.returnTypeConverter)).apply(result) :
                                result);

                AsyncFunction<Object, TransportMessage> f = new AsyncFunction<Object, TransportMessage>() {

                    @Override
                    public ListenableFuture<TransportMessage> apply(final Object input) throws Exception {
                        return Global.executor.submit(new Callable<TransportMessage>() {

                            @Override
                            public TransportMessage call() throws Exception {
                                Message responseMessage = protocol.createResponseMessage(requestMessage, new Message.ResponseObject(input, false));
                                response.setPayload(responseMessage.getMessageData());
                                response.setContentType(protocol.getMimeType());
                                return response;
                            }
                        });
                    }
                };
                return Futures.transform(futureResult, f);
            }
        }

        Message responseMessage = protocol.createResponseMessage(requestMessage, new Message.ResponseObject(result, isException));
        response.setPayload(responseMessage.getMessageData());
        response.setContentType(protocol.getMimeType());
        return Futures.immediateFuture(response);
    }
}

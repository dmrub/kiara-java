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
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.*;
import de.dfki.kiara.config.ProtocolInfo;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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

    @Override
    public void close() {
    }

    void performCall(TransportMessage request, TransportMessage response) throws IOException, IllegalAccessException, IllegalArgumentException {
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
        final MethodEntry methodEntry = serviceMethodBinder.getMethodEntry();

        final List<Object> args = requestMessage.getRequestObject(methodEntry.serializationParamTypes).args;

        if (methodEntry.hasFutureParams) {
            final int numArgs = args.size();
            for (int i = 0; i < numArgs; ++i) {
                if (methodEntry.isFutureParam.get(i)) {
                    System.err.println("FUP SPT "+methodEntry.serializationParamTypes[i]);
                    final Function<Object, Object> f = ((Function<Object, Object>) methodEntry.serializationToParamConverters[i]);
                    args.set(i, f.apply(args.get(i)));
                }
            }
        }

        Object result;
        boolean isException = false;
        try {
            result = serviceMethodBinder.getBoundMethod().invoke(serviceMethodBinder.getImplementedClass(), args.toArray());
        } catch (InvocationTargetException ex) {
            isException = true;
            result = ex.getTargetException();
        }
        Message responseMessage = protocol.createResponseMessage(requestMessage, new Message.ResponseObject(result, isException));
        response.setPayload(responseMessage.getMessageData());
        response.setContentType(protocol.getMimeType());
    }
}

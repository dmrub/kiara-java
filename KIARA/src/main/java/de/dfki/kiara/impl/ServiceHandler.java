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

import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.ProtocolRegistry;
import de.dfki.kiara.Service;
import de.dfki.kiara.ServiceMethodBinder;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.config.ProtocolInfo;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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

        Message requestMessage = protocol.createRequestMessageFromData(request.getPayload());
        String methodName = requestMessage.getMethodName();

        ServiceMethodBinder serviceMethod = service.getMethodBinding().getServiceMethod(methodName);

        Object result;
        boolean isException = false;
        try {
            result = serviceMethod.getBoundMethod().invoke(
                    serviceMethod.getImplementedClass(), requestMessage.getRequestObject(serviceMethod.getBoundMethod().getParameterTypes()).args.toArray());
        } catch (InvocationTargetException ex) {
            isException = true;
            result = ex.getTargetException();
        }
        Message responseMessage = protocol.createResponseMessage(requestMessage, new Message.ResponseObject(result, isException));
        response.setPayload(responseMessage.getMessageData());
        response.setContentType(protocol.getMimeType());
    }
}

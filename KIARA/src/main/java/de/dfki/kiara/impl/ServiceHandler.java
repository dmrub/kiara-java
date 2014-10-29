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
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;

import de.dfki.kiara.*;
import de.dfki.kiara.config.ProtocolInfo;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServiceHandler implements Closeable {
    private final ServiceImpl service;
    private final ProtocolInfo protocolInfo;
    private final Protocol protocol;

    public ServiceHandler(ServiceImpl service, String protocolName) throws InstantiationException, IllegalAccessException {
        this.service = service;
        this.protocolInfo = new ProtocolInfo();
        this.protocolInfo.name = protocolName;
        this.protocol = ProtocolRegistry.newProtocolByName(protocolName);
    }

    public Service getService() {
        return service;
    }

    public de.dfki.kiara.ServiceMethodExecutor getServiceMethodExecutor() {
        return service.getMethodExecutor();
    }

    public ServiceMethodExecutorImpl getServiceMethodBinding() {
        return service.getMethodExecutor();
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

    public ListenableFuture<Message> performLocalCall(final InvocationEnvironment env, final Message requestMessage) throws IOException, IllegalAccessException, IllegalArgumentException, ExecutionException, InterruptedException {
        return service.getMethodExecutor().performLocalCall(env, requestMessage);
    }
}

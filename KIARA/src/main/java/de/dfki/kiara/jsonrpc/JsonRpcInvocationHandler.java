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
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcInvocationHandler extends DefaultInvocationHandler<JsonRpcProtocol> {

    public JsonRpcInvocationHandler(Connection connection, InterfaceMapping<?> interfaceMapping, JsonRpcProtocol protocol) {
        super(connection, interfaceMapping, protocol, new MessageDecoder<>(protocol));
    }

    @Override
    public MessageDispatcher createMessageDispatcher(Message request) {
        return new JsonRpcMessageDispatcher(protocol, ((JsonRpcMessage) request).getId());
    }
}

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

import de.dfki.kiara.Connection;
import de.dfki.kiara.ConnectionException;
import de.dfki.kiara.InterfaceCodeGen;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.MethodBinder;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.jos.JosProtocol;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ConnectionImpl implements Connection {

    private static final Map<String, Class<? extends Protocol>> protocolRegistry = new HashMap<>();
    private final Protocol protocol;

    static {
        // initialize protocols
        protocolRegistry.put("jsonrpc", JsonRpcProtocol.class);
        protocolRegistry.put("javaobjectstream", JosProtocol.class);
    }

    ConnectionImpl(String url) throws ConnectionException {
        // 1. perform negotiation
        // TODO

        // 2. select implementation
        String protocolName = "jsonrpc";
        //String protocolName = "javaobjectstream";
        Class<? extends Protocol> protocolClass = protocolRegistry.get(protocolName);
        if (protocolClass == null)
            throw new ConnectionException("Unsupported protocol '"+protocolName+"'");

        try {
            protocol = protocolClass.newInstance();
        } catch (InstantiationException ex) {
            throw new ConnectionException("Could not instantiate protocol", ex);
        } catch (IllegalAccessException ex) {
            throw new ConnectionException("Could not instantiate protocol", ex);
        }

        protocol.initConnection(this);
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public <T> T generateClientFunctions(MethodBinder<T> methodBinder) {
        InterfaceMapping<T> mapping = new InterfaceMapping<>(methodBinder);
        Class<T> interfaceClass = mapping.getInterfaceClass();

        InterfaceCodeGen codegen = protocol.getInterfaceCodeGen();
        return codegen.generateInterfaceImpl(interfaceClass, mapping);
    }

}

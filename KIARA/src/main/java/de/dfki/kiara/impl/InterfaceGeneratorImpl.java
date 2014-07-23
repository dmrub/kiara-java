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

import de.dfki.kiara.jsonrpc.JsonRpcInvocationHandler;
import com.google.common.reflect.Reflection;
import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceGenerator;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;

/**
 * @param <T>
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class InterfaceGeneratorImpl<T> implements InterfaceGenerator<T> {

    private final InterfaceMapping<T> interfaceMapping;

    public InterfaceGeneratorImpl(InterfaceMapping<T> interfaceMapping) {
        this.interfaceMapping = interfaceMapping;
    }

    @Override
    public T generateClientFunctions(Connection connection) {
        Class<T> interfaceClass = interfaceMapping.getInterfaceClass();
        return Reflection.newProxy(interfaceClass,
                new JsonRpcInvocationHandler(connection, interfaceMapping, new JsonRpcProtocol()));
    }

}

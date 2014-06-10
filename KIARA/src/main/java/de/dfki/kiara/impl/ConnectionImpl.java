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

import com.google.common.reflect.Reflection;
import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceGenerator;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.MethodBinder;
import de.dfki.kiara.RemoteInterface;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ConnectionImpl implements Connection {

    ConnectionImpl(String url) {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public <T> T generateClientFunctions(MethodBinder<T> methodBinder) {
        InterfaceMapping<T> mapping = new InterfaceMapping<>(methodBinder);
        Class<T> interfaceClass = mapping.getInterfaceClass();

        Object impl = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[] {interfaceClass, RemoteInterface.class},
                new SerializationInvocationHandler(mapping));
        return interfaceClass.cast(impl);
    }

}

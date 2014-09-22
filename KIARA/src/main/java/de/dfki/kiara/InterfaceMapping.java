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
package de.dfki.kiara;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class InterfaceMapping<T> {

    private final Class<T> interfaceClass;
    private final Map<Method, String> boundMethods;
    private final Map<Method, MethodEntry> methodEntries;

    /**
     * @param binder
     */
    public InterfaceMapping(MethodBinding<T> binder) {
        interfaceClass = binder.getInterfaceClass();
        boundMethods = new HashMap<>(binder.getBoundMethods());
        methodEntries = new HashMap<>(boundMethods.size());

        for (Method method : boundMethods.keySet()) {
            methodEntries.put(method, new MethodEntry(method));
        }
    }

    public final Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public final Map<Method, String> getBoundMethods() {
        return boundMethods;
    }

    public final String getIDLMethodName(Method method) {
        return boundMethods.get(method);
    }

    public final MethodEntry getMethodEntry(Method method) {
        return methodEntries.get(method);
    }

    public final Method getMethod(String idlMethodName) {
        for (Map.Entry<Method, String> e : boundMethods.entrySet()) {
            if (e.getValue().equals(idlMethodName)) {
                return e.getKey();
            }
        }
        return null;
    }
}

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
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <T>
 */
public final class MethodBinding<T> {
    private final Class<T> interfaceClass;
    private final Map<String, Method> uniqueMethodMap; // maps Java method name ot Method object
    private final Map<Method, String> boundMethods; // maps Java method to IDL method name

    public MethodBinding(Class<T> interfaceClass) {
        if (interfaceClass == null)
            throw new NullPointerException("Interface class can't be null");
        if (!interfaceClass.isInterface())
            throw new IllegalArgumentException("Passed class is not an interface");
        this.interfaceClass = interfaceClass;
        Set<String> duplicateMethodNames = new HashSet<>();
        this.uniqueMethodMap = new HashMap<>();
        this.boundMethods = new HashMap<>();
        final Method[] methods = interfaceClass.getMethods();
        for (Method m : methods) {
            String methodName = m.getName();
            if (duplicateMethodNames.contains(methodName))
                continue;
            if (uniqueMethodMap.containsKey(methodName)) {
                uniqueMethodMap.remove(methodName);
                duplicateMethodNames.add(methodName);
            }
            uniqueMethodMap.put(methodName, m);
        }
    }

    public final MethodBinding<T> bind(String idlMethodName, String methodName) throws NoSuchMethodException, SecurityException {
        Method method = uniqueMethodMap.get(methodName);
        if (method == null)
            throw new NoSuchMethodException("No method '"+methodName+"' in class "+interfaceClass.getName());
        if (boundMethods.containsKey(method))
            throw new IllegalArgumentException("Method '"+methodName+"' was already bound");
        boundMethods.put(method, idlMethodName);
        return this;
    }

    public final MethodBinding<T> bind(String idlMethodName, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method = interfaceClass.getMethod(methodName, parameterTypes);
                if (boundMethods.containsKey(method))
            throw new IllegalArgumentException("Method '"+methodName+"' was already bound");
        boundMethods.put(method, idlMethodName);
        return this;
    }

    public final Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public final Map<Method, String> getBoundMethods() {
        return boundMethods;
    }
}

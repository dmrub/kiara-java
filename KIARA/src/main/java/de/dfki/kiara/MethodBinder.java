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

package de.dfki.kiara;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class MethodBinder {
    private final Class<?> interfaceClass;
    private final Map<String, Method> uniqueMethodMap;
    private final Map<String, Method> boundMethods; // maps IDL method to Java method

    private MethodBinder(Class<?> interfaceClass) {
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

    static public final MethodBinder create(Class<?> interfaceClass) {
        return new MethodBinder(interfaceClass);
    }

    public final MethodBinder bind(String idlMethodName, String methodName) throws NoSuchMethodException, SecurityException {
        Method method = uniqueMethodMap.get(methodName);
        if (method == null)
            throw new NoSuchMethodException("No method '"+methodName+"' in class "+interfaceClass.getName());
        boundMethods.put(idlMethodName, method);
        return this;
    }

    public final MethodBinder bind(String idlMethodName, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method = interfaceClass.getMethod(methodName, parameterTypes);
        boundMethods.put(idlMethodName, method);
        return this;
    }

    public final Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public final Map<String, Method> getBoundMethods() {
        return boundMethods;
    }
}

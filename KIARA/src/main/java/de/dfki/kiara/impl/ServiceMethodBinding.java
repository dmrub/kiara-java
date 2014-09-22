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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.dfki.kiara.ServiceMethodBinder;

/**
 * @author shahzad
 */
public class ServiceMethodBinding {

    private final Map<String, ServiceMethodBinder> internalMapping;

    public ServiceMethodBinding() {
        internalMapping = new HashMap<>();
    }

    public void bindServiceMethod(String idlMethodName, Object serviceClass,
                                  String serviceMethodName) throws NoSuchMethodException, SecurityException {
        Method method = null;
        for (Method m : serviceClass.getClass().getMethods()) {
            if (m.getName().equals(serviceMethodName)) {
                method = m;
                break;
            }
        }

        if (method == null)
            throw new IllegalArgumentException("No such method "+serviceMethodName+" in class "+serviceClass.getClass().getName());

        internalMapping.put(idlMethodName, new ServiceMethodBinder(serviceClass, method));
    }

    public void bindServiceMethod(String idlMethodName, Object serviceClass,
                                  String serviceMethodName, Class<?>[] parameterTypes) throws
            NoSuchMethodException, SecurityException {
        Method method = serviceClass.getClass().getMethod(serviceMethodName, parameterTypes);
        if (method == null)
            throw new IllegalArgumentException("No such method "+serviceMethodName+" with parameter types "+parameterTypes+" in class "+serviceClass.getClass().getName());
        internalMapping.put(idlMethodName, new ServiceMethodBinder(serviceClass, method));
    }

    public ServiceMethodBinder getServiceMethodBinder(String idlMethodName) {
        return internalMapping.get(idlMethodName);
    }

    public void unbindServiceMethod(String idlMethodName) throws NoSuchMethodException {
        if (internalMapping.containsKey(idlMethodName)) {
            internalMapping.remove(idlMethodName);
        } else {
            throw new NoSuchMethodException();
        }
    }
}

/*
 * Copyright (C) 2014 shahzad
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

import de.dfki.kiara.ServiceMethodBinder;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shahzad
 */
public class ServiceMethodBinding {

    private Map<String, ServiceMethodBinder> internalMapping = null;

    public ServiceMethodBinding() {
        internalMapping = new HashMap<>();
    }

    public void bindServiceMethod(String idlMethodName, Object serviceClass,
            String serviceMethodName) throws NoSuchMethodException, SecurityException {
        Method method = null;
        for(Method m: serviceClass.getClass().getMethods()){
            if(m.getName().equals(serviceMethodName)){
                method = m;
                break;
            }
        }
        internalMapping.put(idlMethodName, new ServiceMethodBinder(serviceClass, method));
    }

    public void bindServiceMethod(String idlMethodName, Object serviceClass,
            String serviceMethodName, Class<?>[] parameterTypes) throws
            NoSuchMethodException, SecurityException {
        Method method = serviceClass.getClass().getMethod(serviceMethodName, parameterTypes);
        internalMapping.put(idlMethodName, new ServiceMethodBinder(serviceClass, method));
    }

    public ServiceMethodBinder getServiceMethod(String idlMethodName) {
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

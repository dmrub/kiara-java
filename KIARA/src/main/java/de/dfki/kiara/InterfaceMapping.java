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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <T>
 */
public final class InterfaceMapping<T> {
    private final Class<T> interfaceClass;
    private final Map<Method, String> boundMethods;
    private final Map<Method, MethodEntry> methodEntries;

    private enum MethodKind {
        SYNCHRONOUS,
        ASYNCHRONOUS,
        SERIALIZER,
        DESERIALIZER
    }

    private final class MethodEntry {
        public final MethodKind kind;

        public MethodEntry(MethodKind kind) {
            this.kind = kind;
        }
    }

    /**
     *
     * @param binder
     */
    public InterfaceMapping(MethodBinder<T> binder) {
        interfaceClass = binder.getInterfaceClass();
        boundMethods = new HashMap<>(binder.getBoundMethods());
        methodEntries = new HashMap<>(boundMethods.size());

        MethodKind kind = MethodKind.SYNCHRONOUS;
        for (Method m : boundMethods.keySet()) {
            // serializers
            if (Util.isSerializer(m))
                kind = MethodKind.SERIALIZER;
            else if (Util.isDeserializer(m)) {
                kind = MethodKind.DESERIALIZER;
            }
            methodEntries.put(m, new MethodEntry(kind));
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

    public final Method getMethod(String idlMethodName) {
        for (Map.Entry<Method, String> e : boundMethods.entrySet()) {
            if (e.getValue().equals(idlMethodName))
                return e.getKey();
        }
        return null;
    }
}

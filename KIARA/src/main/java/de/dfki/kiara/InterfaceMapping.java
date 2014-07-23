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

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
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

    public enum MethodKind {

        SYNCHRONOUS,
        ASYNCHRONOUS,
        SERIALIZER,
        DESERIALIZER
    }

    public enum ResultTypeKind {

        DEFAULT,
        FUTURE,
        LISTENING
    }

    public final class MethodEntry {

        public final MethodKind kind;
        public final java.lang.reflect.Type futureParamOfReturnType;
        public final boolean hasFutureParams;
        public final boolean hasListeningFutureParams;
        public final Class<?>[] serializationParamTypes;
        public final Function<?, ?>[] paramConverters;

        public MethodEntry(MethodKind kind,
                java.lang.reflect.Type futureParamOfReturnType,
                boolean hasFutureParams,
                boolean hasListeningFutureParams,
                Class<?>[] serializationParamTypes,
                Function<?, ?>[] paramConverters) {
            this.kind = kind;
            this.futureParamOfReturnType = futureParamOfReturnType;
            this.hasFutureParams = hasFutureParams;
            this.hasListeningFutureParams = hasListeningFutureParams;
            this.serializationParamTypes = serializationParamTypes;
            this.paramConverters = paramConverters;
        }
    }

    /**
     *
     * @param binder
     */
    public InterfaceMapping(MethodBinding<T> binder) {
        interfaceClass = binder.getInterfaceClass();
        boundMethods = new HashMap<>(binder.getBoundMethods());
        methodEntries = new HashMap<>(boundMethods.size());

        MethodKind kind;
        java.lang.reflect.Type futureParamOfReturnType;
        boolean hasFutureParams;
        boolean hasListeningFutureParams;
        for (Method m : boundMethods.keySet()) {
            kind = MethodKind.SYNCHRONOUS;
            futureParamOfReturnType = null;
            hasFutureParams = false;
            hasListeningFutureParams = false;

            // serializers
            if (Util.isSerializer(m)) {
                kind = MethodKind.SERIALIZER;
            } else if (Util.isDeserializer(m)) {
                kind = MethodKind.DESERIALIZER;
            }
            java.lang.reflect.Type genericReturnType = m.getGenericReturnType();
            futureParamOfReturnType = genericReturnType != null ? Util.getFutureParameterType(genericReturnType) : null;

            // check for Future
            final java.lang.reflect.Type[] genericParamTypes = m.getGenericParameterTypes();
            final Class<?>[] serParamTypes = new Class<?>[genericParamTypes.length];
            final Function<?, ?>[] paramConverters = new Function<?, ?>[genericParamTypes.length];

            Util.ClassAndConverter classAndConverter = null;

            for (int i = 0; i < genericParamTypes.length; ++i) {
                classAndConverter = Util.dereferenceFutureTypeAndCreateConverter(genericParamTypes[i]);
                java.lang.reflect.Type futureParamType = Util.getFutureParameterType(genericParamTypes[i]);
                if (futureParamType != null) {
                    hasFutureParams = true;
                    futureParamType = Util.getListenableFutureParameterType(genericParamTypes[i]);
                    if (futureParamType != null) {
                        hasListeningFutureParams = true;
                    }
                }

                if (classAndConverter == null) {
                    classAndConverter = new Util.ClassAndConverter(
                            Util.toClass(genericParamTypes[i]),
                            new Function<Object, Object>() {

                                @Override
                                public Object apply(Object input) {
                                    return Futures.immediateFuture(input);
                                }

                            });
                }
                serParamTypes[i] = classAndConverter.paramType;
                paramConverters[i] = classAndConverter.paramConverter;
            }
            //System.err.format("Param classes: %s%n", Arrays.toString(serParamTypes));

            methodEntries.put(m, new MethodEntry(kind, futureParamOfReturnType, hasFutureParams, hasListeningFutureParams, serParamTypes, paramConverters));
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

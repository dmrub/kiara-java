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

import java.lang.reflect.Method;
import java.util.BitSet;

/**
* Created by Dmitri Rubinstein on 7/31/14.
*/
public class MethodEntry {

    public enum MethodKind {

        DEFAULT,
        SERIALIZER,
        DESERIALIZER
    }

    public final MethodKind kind;
    public final java.lang.reflect.Type futureParamOfReturnType;
    public final Function<?, ?> returnTypeConverter;
    public final boolean hasFutureParams;
    public final boolean hasListeningFutureParams;
    public final Class<?>[] serializationParamTypes;
    public final Function<?, ?>[] paramConverters;
    public final Function<?, ?>[] serializationToParamConverters;
    public final BitSet isFutureParam;

    /*
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
    */

    public MethodEntry(Method method) {
        MethodKind kind;
        boolean hasFutureParams;

        boolean hasListeningFutureParams;
        java.lang.reflect.Type futureParamOfReturnType;
        kind = MethodKind.DEFAULT;
        hasFutureParams = false;
        hasListeningFutureParams = false;
        Function<?, ?> returnTypeConverter = null;

        // serializers
        if (Util.isSerializer(method)) {
            kind = MethodKind.SERIALIZER;
        } else if (Util.isDeserializer(method)) {
            kind = MethodKind.DESERIALIZER;
        }
        java.lang.reflect.Type genericReturnType = method.getGenericReturnType();
        futureParamOfReturnType = genericReturnType != null ? Util.getFutureParameterType(genericReturnType) : null;

        if (futureParamOfReturnType != null) {
            if (!Util.isListenableFuture(genericReturnType)) {
                returnTypeConverter = new Util.ConvertFutureToListenableFuture();
            }
        }

        // check for Future
        final java.lang.reflect.Type[] genericParamTypes = method.getGenericParameterTypes();
        final Class<?>[] serializationParamTypes = new Class<?>[genericParamTypes.length];
        final Function<?, ?>[] paramConverters = new Function<?, ?>[genericParamTypes.length];
        final Function<?, ?>[] serializationToParamConverters = new Function<?, ?>[genericParamTypes.length];
        final BitSet isFutureParam = new BitSet(genericParamTypes.length);
        Util.ClassAndConverters classAndConverters;

        for (int i = 0; i < genericParamTypes.length; ++i) {
            classAndConverters = Util.getSerializationTypeAndCreateConverters(genericParamTypes[i]);
            java.lang.reflect.Type futureParamType = Util.getFutureParameterType(genericParamTypes[i]);
            if (futureParamType != null) {
                hasFutureParams = true;
                isFutureParam.set(i, true);
                futureParamType = Util.getListenableFutureParameterType(genericParamTypes[i]);
                if (futureParamType != null) {
                    hasListeningFutureParams = true;
                }
            }

            serializationParamTypes[i] = classAndConverters.serializationParamType;
            paramConverters[i] = classAndConverters.paramToFutureConverter;
            serializationToParamConverters[i] = classAndConverters.serializationToParamConverter;
        }
        //System.err.format("Param classes: %s%n", Arrays.toString(serializationParamTypes));

        this.kind = kind;
        this.futureParamOfReturnType = futureParamOfReturnType;
        this.hasFutureParams = hasFutureParams;
        this.hasListeningFutureParams = hasListeningFutureParams;
        this.serializationParamTypes = serializationParamTypes;
        this.paramConverters = paramConverters;
        this.serializationToParamConverters = serializationToParamConverters;
        this.isFutureParam = isFutureParam;
        this.returnTypeConverter = returnTypeConverter;
    }

}

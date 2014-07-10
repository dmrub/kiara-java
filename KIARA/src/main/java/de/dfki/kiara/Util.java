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

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class Util {

    private static final TypeToken<Future<?>> futureTok = new TypeToken<Future<?>>() {
    };

    private Util() {

    }

    public static Type getFutureParameterType(java.lang.reflect.Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType ptype = (ParameterizedType) type;
        return futureTok.isAssignableFrom(ptype) ? ptype.getActualTypeArguments()[0] : null;
    }

    public static Class<?> toClass(java.lang.reflect.Type type) {
        if (type instanceof ParameterizedType)
            return (Class<?>)((ParameterizedType)type).getRawType();
        if (type instanceof Class)
            return (Class<?>)type;
        return null;
    }

    public static boolean isSerializer(Method method) {
        return method.getReturnType().equals(de.dfki.kiara.Message.class);
    }

    public static boolean isDeserializer(Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        return paramTypes.length == 1 && paramTypes[0].equals(de.dfki.kiara.Message.class);
    }

}

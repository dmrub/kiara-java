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
import com.google.common.base.Functions;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class Util {

    private static final TypeToken<Future<?>> futureTok = new TypeToken<Future<?>>() {
    };
    private static final TypeToken<ListenableFuture<?>> listenableFutureTok = new TypeToken<ListenableFuture<?>>() {
    };

    private Util() {

    }

    public static Type getFutureParameterType(java.lang.reflect.Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        return futureTok.isAssignableFrom(type) ? ((ParameterizedType) type).getActualTypeArguments()[0] : null;
    }

    public static Type getListenableFutureParameterType(java.lang.reflect.Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        return listenableFutureTok.isAssignableFrom(type) ? ((ParameterizedType) type).getActualTypeArguments()[0] : null;
    }

    public static class ClassAndConverter {

        public final Class<?> paramType;
        public final Function<Object, Object> paramConverter;

        public ClassAndConverter(Class<?> paramClass, Function<Object, Object> paramConverter) {
            this.paramType = paramClass;
            this.paramConverter = paramConverter;
        }

    }

    private static class DereferenceListenableFuture implements Function<Object, Object> {

        @Override
        public Object apply(Object input) {
            ListenableFuture<ListenableFuture<?>> future = (ListenableFuture<ListenableFuture<?>>) input;
            return Futures.dereference(future);
        }

    }

    private static class ConvertFutureToListenableFuture implements Function<Object, Object> {

        @Override
        public Object apply(Object input) {
            if (input instanceof ListenableFuture<?>) {
                return input;
            }
            return JdkFutureAdapters.listenInPoolThread((Future<?>) input);
        }

    }

    public static Class<?> dereferenceFutureType(java.lang.reflect.Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        if (!futureTok.isAssignableFrom(type) && !listenableFutureTok.isAssignableFrom(type)) {
            return null;
        }
        java.lang.reflect.Type paramType = ((ParameterizedType) type).getActualTypeArguments()[0];
        while (futureTok.isAssignableFrom(paramType) || listenableFutureTok.isAssignableFrom(paramType)) {
            paramType = ((ParameterizedType) paramType).getActualTypeArguments()[0];
        }
        return toClass(paramType);
    }

    public static ClassAndConverter dereferenceFutureTypeAndCreateConverter(java.lang.reflect.Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        boolean isFuture = futureTok.isAssignableFrom(type);
        if (!isFuture) {
            return null;
        }
        boolean isListenableFuture = listenableFutureTok.isAssignableFrom(type);
        if (!isListenableFuture) {
            return null;
        }
        java.lang.reflect.Type paramType = ((ParameterizedType) type).getActualTypeArguments()[0];
        Function<Object, Object> paramConverter = null;
        if (!isListenableFuture) {
            paramConverter = new ConvertFutureToListenableFuture();
        }
        while (isFuture) {
            isFuture = futureTok.isAssignableFrom(paramType);
            if (isFuture) {
                paramType = ((ParameterizedType) paramType).getActualTypeArguments()[0];
                isListenableFuture = listenableFutureTok.isAssignableFrom(paramType);
                if (!isListenableFuture) {
                    Function<Object, Object> tmp = new ConvertFutureToListenableFuture();
                    if (paramConverter == null) {
                        paramConverter = tmp;
                    } else {
                        paramConverter = Functions.compose(tmp, paramConverter);
                    }
                }
            }
        }
        return new ClassAndConverter(toClass(paramType), paramConverter == null ? Functions.<Object>identity() : paramConverter);
    }

    public static Class<?> toClass(java.lang.reflect.Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        return null;
    }

    public static boolean isSerializer(Method method) {
        return method.getReturnType().equals(de.dfki.kiara.Message.class);
    }

    public static boolean isDeserializer(Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        return paramTypes.length == 1 && paramTypes[0].equals(de.dfki.kiara.Message.class);
    }

    public static String bufferToString(ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset(), buffer.remaining());
        } else {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return new String(bytes);
        }
    }

    public static String bufferToString(ByteBuffer buffer, String charsetName) throws UnsupportedEncodingException {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset(), buffer.remaining(), charsetName);
        } else {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return new String(bytes, charsetName);
        }
    }
}

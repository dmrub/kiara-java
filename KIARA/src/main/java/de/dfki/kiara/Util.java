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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class Util {

    private static final TypeToken<Future<?>> futureTok = new TypeToken<Future<?>>() {
    };
    private static final TypeToken<ListenableFuture<?>> listenableFutureTok = new TypeToken<ListenableFuture<?>>() {
    };

    private static final Map<TypeToken<?>, ClassAndConverters> convCache = new HashMap<>();

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

    public static boolean isListenableFuture(java.lang.reflect.Type type) {
        return listenableFutureTok.isAssignableFrom(type);
    }

    public static class ClassAndConverters {

        /*
         * Non future type that can be used in serialization
         */
        public final TypeToken<?> serializationParamType;
        /*
         *
         * Converts recursively
         *          Future<X> -> ListenableFuture<X>
         *          X -> X
         *
         */
        public final Function<Object, Object> paramToFutureConverter;

        public final Function<Object, Object> serializationToParamConverter;

        public ClassAndConverters(TypeToken<?> paramClass, Function<Object, Object> paramToFutureConverter,
                Function<Object, Object> serializationToParamConverter) {
            this.serializationParamType = paramClass;
            this.paramToFutureConverter = paramToFutureConverter;
            this.serializationToParamConverter = serializationToParamConverter;
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

    private static final Function<Object, Object> futureToListenableFutureConverter = new ConvertFutureToListenableFuture();

    public static Function<Object, Object> getFutureToListenableFutureConverter() {
        return futureToListenableFutureConverter;
    }

    private static class ConvertTypeToListenableFuture implements Function<Object, Object> {

        @Override
        public Object apply(Object input) {
            return Futures.immediateFuture(input);
        }

    }

    public static ClassAndConverters getSerializationTypeAndCreateConverters(java.lang.reflect.Type type) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        boolean isFuture = futureTok.isAssignableFrom(type);
        if (!(type instanceof ParameterizedType) || !isFuture) {
            return new ClassAndConverters(
                    TypeToken.of(type),
                    new Function<Object, Object>() {

                        @Override
                        public Object apply(Object input) {
                            return Futures.immediateFuture(input);
                        }

                    }, null);
        }

        TypeToken<?> typeToken = TypeToken.of(type);
        if (typeToken != null) {
            ClassAndConverters conv;
            synchronized (convCache) {
                conv = convCache.get(typeToken);
            }
            if (conv != null) {
                return conv;
            }
        }

        java.lang.reflect.Type paramType = ((ParameterizedType) type).getActualTypeArguments()[0];

        boolean isListenableFuture = listenableFutureTok.isAssignableFrom(type);
        Function<Object, Object> paramConverter = null;
        Function<Object, Object> serializationToParamConverter = null;
        if (!isListenableFuture) {
            // rule ListenableFuture<X> -> Future<X>
            paramConverter = new ConvertFutureToListenableFuture();
        }
        if (isFuture) {
            // rule X -> ListenableFuture<X>
            serializationToParamConverter = new ConvertTypeToListenableFuture();
        }
        while (isFuture) {
            isFuture = futureTok.isAssignableFrom(paramType);
            if (isFuture) {
                paramType = ((ParameterizedType) paramType).getActualTypeArguments()[0];
                isListenableFuture = listenableFutureTok.isAssignableFrom(paramType);
                if (!isListenableFuture) {
                    Function<Object, Object> tmp = getFutureToListenableFutureConverter();
                    if (paramConverter == null) {
                        paramConverter = tmp;
                    } else {
                        paramConverter = Functions.compose(tmp, paramConverter);
                    }
                }
                Function<Object, Object> tmp = new ConvertTypeToListenableFuture();
                if (serializationToParamConverter == null) {
                    serializationToParamConverter = tmp;
                } else {
                    serializationToParamConverter = Functions.compose(tmp, serializationToParamConverter);
                }
            }
        }

        ClassAndConverters conv = new ClassAndConverters(TypeToken.of(paramType),
                paramConverter == null ? Functions.<Object>identity() : paramConverter,
                serializationToParamConverter);
        if (typeToken != null) {
            synchronized (convCache) {
                convCache.put(typeToken, conv);
            }
        }
        return conv;
    }

    public static boolean isSerializer(Method method) {
        return method.getReturnType().equals(de.dfki.kiara.Message.class);
    }

    public static boolean isDeserializer(Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        return paramTypes.length == 1 && paramTypes[0].equals(de.dfki.kiara.Message.class);
    }

    public static ByteBuffer stringToBuffer(String string, String charsetName) throws UnsupportedEncodingException {
        return ByteBuffer.wrap(string.getBytes(charsetName));
    }

    public static String bufferToString(ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset(), buffer.remaining());
        } else {
            int oldPos = buffer.position();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            buffer.position(oldPos);
            return new String(bytes);
        }
    }

    public static String bufferToString(ByteBuffer buffer, String charsetName) throws UnsupportedEncodingException {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset(), buffer.remaining(), charsetName);
        } else {
            int oldPos = buffer.position();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            buffer.position(oldPos);
            return new String(bytes, charsetName);
        }
    }
}

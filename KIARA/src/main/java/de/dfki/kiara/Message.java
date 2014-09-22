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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public interface Message {

    public static enum Kind {
        REQUEST,
        RESPONSE,
        EXCEPTION
    }

    public static class RequestObject {
        public final String methodName;
        public final List<Object> args;

        public RequestObject(String methodName, Object[] args) {
            this(methodName, Arrays.asList(args));
        }

        public RequestObject(String methodName, List<Object> args) {
            this.methodName = methodName;
            this.args = args;
        }
    }

    public static class ResponseObject {
        public final Object result;
        public final boolean isException;

        public ResponseObject(Object result, boolean isException) {
            this.result = result;
            this.isException = isException;
        }
    }

    public Kind getMessageKind();

    public Protocol getProtocol();

    public String getMethodName();

    /** Stores to the byte buffer message representation that can be sent over network.
     *
     * @return
     * @throws java.io.IOException
     */
    public ByteBuffer getMessageData() throws IOException;

    public RequestObject getRequestObject(Class<?>[] paramTypes) throws MessageDeserializationException;

    public ResponseObject getResponseObject(Class<?> returnType) throws MessageDeserializationException;

    public void setGenericError(int errorCode, String errorMessage);
}

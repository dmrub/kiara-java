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

package de.dfki.kiara.jsonrpc;

import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcMessage implements Message {

    private final Protocol protocol;
    private final String methodName;
    private final ByteBuffer data;

    public JsonRpcMessage(Protocol protocol, String methodName, ByteBuffer data) {
        this.protocol = protocol;
        this.methodName = methodName;
        this.data = data;
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public ByteBuffer getMessageData() {
        return data;
    }

    @Override
    public void setGenericError(int errorCode, String errorMessage) {
    }

    @Override
    public boolean isErrorResponse() {
        return false;
    }

    @Override
    public Kind getMessageKind() {
        return Kind.RESPONSE;
    }

    @Override
    public RequestObject getRequestObject() {
        return null;
    }

    @Override
    public ResponseObject getResponseObject() {
        return null;
    }

}

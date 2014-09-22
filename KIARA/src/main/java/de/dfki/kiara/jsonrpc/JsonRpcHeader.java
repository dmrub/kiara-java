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
package de.dfki.kiara.jsonrpc;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
@JsonSerialize(using = JsonRpcHeaderSerializer.class)
public class JsonRpcHeader {

    private final String method;
    private final List<Object> params;
    private final Object result;
    private final JsonRpcError error;
    private final Object id;

    public JsonRpcHeader(String method, List<Object> params, Object id) {
        this.method = method;
        this.params = params;
        this.result = null;
        this.error = null;
        this.id = id;
    }

    public JsonRpcHeader(Object result, Object id) {
        this.method = null;
        this.params = null;
        this.result = result;
        this.error = null;
        this.id = id;
    }

    public JsonRpcHeader(JsonRpcError error, Object id) {
        this.method = null;
        this.params = null;
        this.result = null;
        this.error = error;
        this.id = id;
    }

    boolean isRequest() {
        return this.method != null;
    }

    boolean isResponse() {
        return this.method == null;
    }

    boolean isResult() {
        return isResponse() && this.error == null;
    }

    boolean isError() {
        return isResponse() && this.error != null;
    }

    public String getMethod() {
        return method;
    }

    public List<Object> getParams() {
        return params;
    }

    public Object getResult() {
        return result;
    }

    public JsonRpcError getError() {
        return error;
    }

    public Object getId() {
        return id;
    }
}

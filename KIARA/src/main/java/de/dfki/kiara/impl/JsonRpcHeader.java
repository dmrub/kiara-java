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

package de.dfki.kiara.impl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
@JsonSerialize(using = JsonRpcHeaderSerializer.class)
public class JsonRpcHeader {
    private final String method;
    private final Object[] params;
    private final Object result;
    private final JsonRpcError error;
    private final Object id;

    public JsonRpcHeader(String method, Object[] params, Object id) {
        this.method = method;
        this.params = params;
        this.result = null;
        this.error = null;
        this.id = id;
    }

    public JsonRpcHeader(String result, JsonRpcError error, Object id) {
        this.method = null;
        this.params = null;
        this.result = result;
        this.error = error;
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParams() {
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

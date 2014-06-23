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

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcError {
    private final int code;
    private final String message;
    private final Object data;

    public static final int PARSE_ERROR      = -32700;  // Invalid JSON was received by the server.
                                                        // An error occurred on the server while parsing the JSON text.
    public static final int INVALID_REQUEST  = -32600;  // The JSON sent is not a valid Request object.
    public static final int METHOD_NOT_FOUND = -32601;  // The method does not exist / is not available.
    public static final int INVALID_PARAMS   = -32602;  // Invalid method parameter(s).
    public static final int INTERNAL_ERROR   = -32603;  // Internal JSON-RPC error.

    public JsonRpcError(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public final int getCode() {
        return code;
    }

    public final Object getData() {
        return data;
    }

    public final String getMessage() {
        return message;
    }
}

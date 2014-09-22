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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcHeaderSerializer extends JsonSerializer<JsonRpcHeader> {

    @Override
    public void serialize(JsonRpcHeader value, JsonGenerator jgen, SerializerProvider sp) throws IOException {
        if (value.isRequest()) {
            // header
            jgen.writeStartObject();
            jgen.writeStringField("jsonrpc", "2.0");
            jgen.writeStringField("method", value.getMethod());
            jgen.writeObjectField("params", value.getParams());
            if (value.getId() != null) {
                jgen.writeObjectField("id", value.getId());
            }
            jgen.writeEndObject();
        } else {
            jgen.writeStartObject();
            jgen.writeStringField("jsonrpc", "2.0");
            if (value.getError() == null) {
                jgen.writeObjectField("result", value.getResult());
            } else {
                jgen.writeObjectField("error", value.getError());
            }
            if (value.getId() != null) {
                jgen.writeObjectField("id", value.getId());
            }
            jgen.writeEndObject();
        }
    }

}

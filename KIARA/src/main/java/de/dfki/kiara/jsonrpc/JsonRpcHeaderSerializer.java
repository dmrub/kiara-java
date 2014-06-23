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

import de.dfki.kiara.jsonrpc.JsonRpcHeader;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcHeaderSerializer extends JsonSerializer<JsonRpcHeader> {

    @Override
    public void serialize(JsonRpcHeader value, JsonGenerator jgen, SerializerProvider sp) throws IOException, JsonProcessingException {
        if (value.getResult() == null) {
            // header
            jgen.writeStartObject();
            jgen.writeStringField("jsonrpc", "2.0");
            jgen.writeStringField("method", value.getMethod());
            jgen.writeObjectField("params", value.getParams());
            if (value.getId() != null)
                jgen.writeObjectField("id", value.getId());
            jgen.writeEndObject();
        } else {
            jgen.writeStartObject();
            jgen.writeStringField("jsonrpc", "2.0");
            if (value.getResult() != null) {
                jgen.writeObjectField("result", value.getResult());
            } else {
                jgen.writeObjectField("error", value.getError());
            }
            if (value.getId() != null)
                jgen.writeObjectField("id", value.getId());
            jgen.writeEndObject();
        }
    }

}

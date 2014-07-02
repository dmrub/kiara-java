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

package de.dfki.kiara.http;

import de.dfki.kiara.TransportMessage;
import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpResponseMessage extends TransportMessage {

    private final HttpHeaders headers;

    HttpResponseMessage(HttpTransportConnection connection, HttpHeaders headers) {
        super(connection);
        this.headers = headers;
    }

    @Override
    public TransportMessage set(String name, Object value) {
        if (Names.CONTENT_TYPE.equals(name)) {
            headers.set(HttpHeaders.Names.CONTENT_TYPE, value);
        } else if (Names.SESSION_ID.equals(name)) {
            headers.set("x-kiara-session", value);
        }
        return this;
    }

    @Override
    public Object get(String name) {
        if (Names.CONTENT_TYPE.equals(name)) {
            return headers.get(HttpHeaders.Names.CONTENT_TYPE);
        } else if (Names.SESSION_ID.equals(name)) {
            return headers.get("x-kiara-session");
        }
        return null;
    }

}

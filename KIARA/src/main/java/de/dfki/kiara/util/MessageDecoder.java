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

package de.dfki.kiara.util;

/**
 * Created by rubinste on 7/30/14.
 */

import de.dfki.kiara.Protocol;
import de.dfki.kiara.TransportMessage;

public class MessageDecoder<PROTOCOL extends Protocol>  implements Pipeline.Handler {

    private final PROTOCOL protocol;

    public MessageDecoder(PROTOCOL protocol) {
        this.protocol = protocol;
    }

    @Override
    public Object process(Object input) throws Exception {
        if (input == null)
            throw new NullPointerException("input");
        if (!(input instanceof TransportMessage))
            throw new IllegalArgumentException("Class: "+input.getClass().toString()+", expected: TransportMessage");
        TransportMessage response = (TransportMessage) input;
        return protocol.createMessageFromData(response.getPayload());
    }
}

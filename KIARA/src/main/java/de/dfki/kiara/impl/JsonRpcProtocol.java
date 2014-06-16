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

import de.dfki.kiara.Connection;
import de.dfki.kiara.Message;
import de.dfki.kiara.Protocol;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class JsonRpcProtocol implements Protocol {

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public Message createRequestMessageFromData(ByteBuffer data) {
        return new JsonRpcMessage(this, null, data);
    }

    @Override
    public Message createResponseMessageFromData(ByteBuffer data) {
        return new JsonRpcMessage(this, null, data);
    }

    @Override
    public Message createRequestMessage(Connection connection, String methodName) {
        return new JsonRpcMessage(this, methodName, null);
    }

    @Override
    public Message createResponseMessage(Connection connection, Message requestMessage) {
        return new JsonRpcMessage(this, null, null);
    }

    @Override
    public void sendMessageSync(Connection connection, Message outMsg, Message inMsg) throws IOException {
    }

}

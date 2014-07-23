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

package de.dfki.kiara;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public interface Protocol {

    public void initConnection(Connection connection);

    public Connection getConnection();

    /**
     *
     * @return MIME Type of the protocol
     */
    public String getMimeType();

    public InterfaceCodeGen getInterfaceCodeGen();

    public Message createRequestMessageFromData(ByteBuffer data) throws IOException;

    public Message createResponseMessageFromData(ByteBuffer data) throws IOException;

    /** Create message for calling service method name.
     *
     * @param connection
     * @param methodName
     * @return
     * @throws java.io.IOException
     */
    public Message createRequestMessage(String methodName) throws IOException;

    public Message createRequestMessage(Message.RequestObject request) throws IOException;

    public Message createResponseMessage(Message requestMessage);

    public Message createResponseMessage(Message.ResponseObject response) throws IOException;

}

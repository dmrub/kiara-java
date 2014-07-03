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

package de.dfki.kiara.test;

import de.dfki.kiara.Kiara;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportRegistry;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportTest {
    public static void main(String[] args) throws Exception {
        Kiara.init();
        try {
            Transport http = TransportRegistry.getTransportByName("http");
            // GET http://localhost:8080/service
            // POST http://localhost:8080/rpc/calc
            TransportConnection c = http.openConnection("http://localhost:8080/rpc/calc", null);
            TransportMessage msg = c.createRequest();
            String request = "{\"jsonrpc\":\"2.0\",\"method\":\"calc.add\",\"params\":[7,11],\"id\":1}";
            msg.setPayload(ByteBuffer.wrap(request.getBytes("UTF-8")));
            msg.setContentType("application/json");
            Future<Void> send = c.send(msg, null);
            c.close();
        } finally {
            Kiara.shutdownGracefully();
        }
    }
}

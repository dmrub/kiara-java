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

import de.dfki.kiara.Handler;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.RequestHandler;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportRegistry;
import de.dfki.kiara.TransportServer;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportServerTest {

    private static class HTTPServerHandler implements Handler<TransportConnection>, RequestHandler<TransportMessage, TransportMessage> {

        @Override
        public boolean onSuccess(TransportConnection result) {
            System.out.printf("Opened connection %s, local address %s, remote address %s%n",
                    result, result.getLocalAddress(), result.getRemoteAddress());

            result.addRequestHandler(this);
            return true;
        }

        @Override
        public boolean onFailure(Throwable t) {
            t.printStackTrace();
            return true;
        }

        @Override
        public TransportMessage onRequest(TransportMessage message) {
            String content = new String(message.getPayload().array(), message.getPayload().arrayOffset(), message.getPayload().remaining());
            System.err.println("Received request (uri="+message.getRequestUri()+" type=" + message.getContentType() + "): " + content);
            TransportMessage response = message.getConnection().createResponse(message);

            String responseText = "WELCOME TO THE WILD WILD WEB SERVER";

            try {
                response.setPayload(ByteBuffer.wrap(responseText.getBytes("UTF-8")));
                response.setContentType("text/plain; charset=UTF-8");
            } catch (UnsupportedEncodingException ex) {

            }
            return response;
        }

    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.util.logging.config.file"));

        Kiara.init();
        try {
            Transport http = TransportRegistry.getTransportByName("http");

            TransportServer server = Kiara.createTransportServer();

            HTTPServerHandler handler = new HTTPServerHandler();
            server.listen("0.0.0.0", "8080", http, handler);
            server.run();

            System.out.println("Server running...");
        } finally {
            //Kiara.shutdownGracefully();
        }
    }
}

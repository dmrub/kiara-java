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
package de.dfki.kiara.testapp;

import de.dfki.kiara.Handler;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportMessageListener;
import de.dfki.kiara.TransportRegistry;
import de.dfki.kiara.TransportServer;
import de.dfki.kiara.config.ServerConfiguration;
import de.dfki.kiara.config.ServerInfo;
import de.dfki.kiara.util.HexDump;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportServerTest {

    static final ServerConfiguration config = new ServerConfiguration();

    static {
        config.info = "test server";
        config.idlContents = "namespace * calc "
                + "service calc { "
                + "    i32 add(i32 a, i32 b) "
                + "    float addf(float a, float b) "
                + "    i32 stringToInt32(string s) "
                + "    string int32ToString(i32 i) "
                + "} ";

        ServerInfo si = new ServerInfo();
        si.services.add("*");
        si.protocol.name = "jsonrpc";
        si.transport.name = "http";
        si.transport.url = "/rpc/calc";
        config.servers.add(si);

        si = new ServerInfo();
        si.services.add("*");
        si.protocol.name = "jsonrpc";
        si.transport.name = "tcp";
        si.transport.url = "tcp://127.0.0.1:8090/rpc/calc";
        config.servers.add(si);
    }

    private static class ServerHandler implements Handler<TransportConnection>, TransportMessageListener {

        @Override
        public boolean onSuccess(TransportConnection result) {
            System.out.printf("Opened connection %s, local address %s, remote address %s%n",
                    result, result.getLocalAddress(), result.getRemoteAddress());

            // result.addRequestHandler(this);
            result.addMessageListener(this);
            return true;
        }

        @Override
        public boolean onFailure(Throwable t) {
            t.printStackTrace();
            return true;
        }

        @Override
        public void onMessage(TransportMessage message) {
            byte[] array;
            int arrayOffset;
            int arrayLength;
            if (message.getPayload().hasArray()) {
                array = message.getPayload().array();
                arrayOffset = message.getPayload().arrayOffset();
                arrayLength = message.getPayloadSize();
            } else {
                array = new byte[message.getPayloadSize()];
                message.getPayload().get(array);
                arrayOffset = 0;
                arrayLength = 0;
            }

            System.err.printf("Received request (message class=%s method=%s uri=%s type=%s): %n%s%n",
                    message.getClass().getName(),
                    message.getHttpMethod(),
                    message.getRequestUri(),
                    message.getContentType(),
                    arrayLength == 0 ? "empty" : HexDump.dumpHexString(array, arrayOffset, arrayLength));
            final TransportConnection tc = message.getConnection();
            final TransportMessage response = tc.createTransportMessage(message);

            String responseText;
            String contentType;
            if ("/service".equals(message.getRequestUri())) {
                try {
                    responseText = config.toJson();
                    contentType = "application/json";
                } catch (IOException ex) {
                    responseText = ex.toString();
                    contentType = "text/plain; charset=UTF-8";
                }
            } else {
                responseText = "WELCOME TO THE WILD WILD WEB SERVER";
                contentType = "text/plain; charset=UTF-8";
            }
//            try {
//                TransportConnection conn = message.getConnection();
//                conn.send(conn.createResponse(message)
//                        .setPayload(ByteBuffer.wrap("TEST123".getBytes("UTF-8")))
//                        .setContentType("text/plain; charset=UTF-8"));
//            } catch (UnsupportedEncodingException ex) {
//
//            }

            try {
                response.setPayload(ByteBuffer.wrap(responseText.getBytes("UTF-8")));
                response.setContentType(contentType);
            } catch (UnsupportedEncodingException ex) {

            }

            tc.send(response);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.util.logging.config.file"));

        Kiara.init();
        try {
            Transport http = TransportRegistry.getTransportByName("http");
            Transport tcp = TransportRegistry.getTransportByName("tcp");

            TransportServer server = Kiara.createTransportServer();

            ServerHandler handler = new ServerHandler();
            server.listen("0.0.0.0", "8080", http, handler);
            server.listen("0.0.0.0", "8090", tcp, handler);
            server.run();

            System.out.println("Server running...");
        } finally {
            //Kiara.shutdownGracefully();
        }
    }
}

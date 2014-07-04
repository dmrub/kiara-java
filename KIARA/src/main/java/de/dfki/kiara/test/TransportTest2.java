/*
 * Copyright (C) 2014 rubinste
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

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportRegistry;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author rubinste
 */
public class TransportTest2 {

    public static void printMessage(TransportMessage message) {
        String content = new String(message.getPayload().array(), message.getPayload().arrayOffset(), message.getPayload().remaining());
        System.err.println("Received message (type=" + message.getContentType() + "): " + content);
    }

    //private static final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    private static final ListeningExecutorService executor = MoreExecutors.sameThreadExecutor();

    private static void closeAll(TransportConnection c) {
        try {
            c.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Kiara.shutdownGracefully();
        }
        executor.shutdownNow();
        System.err.println("PROGRAM FINISHED");
    }

    public static void main(String[] args) throws Exception {

        System.out.println(System.getProperty("java.util.logging.config.file"));

        Kiara.init();
        Transport http = TransportRegistry.getTransportByName("http");
        // GET http://localhost:8080/service
        // POST http://localhost:8080/rpc/calc
        final TransportConnection c = http.openConnection("http://localhost:8080/rpc/calc", null).get();
        TransportMessage msg = c.createRequest();
        String request = "{\"jsonrpc\":\"2.0\",\"method\":\"calc.add\",\"params\":[1,2],\"id\":1}";
        msg.setPayload(ByteBuffer.wrap(request.getBytes("UTF-8")));
        msg.setContentType("application/json");
        msg.setSessionId("SID1");

        c.send(msg).get();
        System.err.println("Message #1 sent");

        TransportMessage response = c.receive(executor).get();
        System.err.println("Message #1 received");
        printMessage(response);

        msg = c.createRequest();
        request = "{\"jsonrpc\":\"2.0\",\"method\":\"calc.add\",\"params\":[3,4],\"id\":2}";
        try {
            msg.setPayload(ByteBuffer.wrap(request.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        msg.setContentType("application/json");

        c.send(msg).get();
        System.err.println("Message #2 sent");

        response = c.receive(executor).get();
        System.err.println("Message #2 received");
        printMessage(response);

        closeAll(c);
    }
}

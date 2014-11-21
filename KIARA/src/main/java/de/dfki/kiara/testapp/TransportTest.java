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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.TransportFactory;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnectionReceiver;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportFactoryRegistry;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TransportTest {

    public static void printMessage(TransportMessage message) {
        String content = new String(message.getPayload().array(), message.getPayload().arrayOffset(), message.getPayload().remaining());
        System.err.println("Received message (type=" + message.getContentType() + "): " + content);
    }

    private static final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    //private static final ListeningExecutorService executor = MoreExecutors.sameThreadExecutor();

    private static void closeAll(Transport c) {
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
        TransportFactory http = TransportFactoryRegistry.getTransportFactoryByName("http");
        // GET http://localhost:8080/service
        // POST http://localhost:8080/rpc/calc
        final TransportConnectionReceiver c = new TransportConnectionReceiver(http.openConnection("http://localhost:8080/rpc/calc", null).get());

        TransportMessage msg = c.createTransportMessage(null);
        String request = "{\"jsonrpc\":\"2.0\",\"method\":\"calc.add\",\"params\":[1,2],\"id\":1}";
        msg.setPayload(ByteBuffer.wrap(request.getBytes("UTF-8")));
        msg.setContentType("application/json");
        msg.setSessionId("SID1");
        ListenableFuture<Void> send = c.send(msg);
        Futures.addCallback(send, new FutureCallback<Void>() {
            @Override
            public void onSuccess(Void v) {
                System.err.println("Message #1 sent");
                try {
                    TransportMessage response = c.receive(executor).get();
                    printMessage(response);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }

                TransportMessage msg = c.createTransportMessage(null);
                String request = "{\"jsonrpc\":\"2.0\",\"method\":\"calc.add\",\"params\":[3,4],\"id\":2}";
                try {
                    msg.setPayload(ByteBuffer.wrap(request.getBytes("UTF-8")));
                } catch (UnsupportedEncodingException ex) {
                    onFailure(null);
                }
                msg.setContentType("application/json");

                Future<Void> send1 = c.send(msg);

                ListenableFuture<TransportMessage> response = c.receive(executor);
                Futures.addCallback(response, new FutureCallback<TransportMessage>() {

                    @Override
                    public void onSuccess(TransportMessage v) {
                        System.err.println("Message #2 received");
                        printMessage(v);
                        closeAll(c);
                    }

                    @Override
                    public void onFailure(Throwable thrwbl) {
                        closeAll(c);
                    }

                });
            }

            @Override
            public void onFailure(Throwable thrwbl) {
            }

        });
    }
}

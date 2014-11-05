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
package de.dfki.kiara.test;

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteException;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;
import de.dfki.kiara.ServerConnection;
import de.dfki.kiara.ServerConnectionListener;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Dmitri Rubinstein on 02/10/14.
 */
@RunWith(Parameterized.class)
public class CallbackTest {

    static {
        System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class CallbackImpl {
        public String add(ServerConnection connection, int a, int b) throws Exception {
            System.out.println("add("+a+","+b+") via connection="+connection);

            MethodBinding<CallbackClient> binder
                    = new MethodBinding<>(CallbackClient.class)
                    .bind("cb.addResult", "addResult");

            CallbackClient cc = connection.getServiceInterface(binder);
            String c = cc.addResult(a, b, a + b);

            return "cb.add: "+c;
        }
    }

    public static class CallbackClientImpl {
        public String addResult(int a, int b, int result) {
            System.out.println("addResult: "+a+" + "+b+" = "+result);
            Assert.assertEquals(a+b, result);
            return "result is "+result;
        }

        public String clientMsg(String msg) {
            System.out.println("cb.clientMsg: " + msg);
            return "cb.clientMsg: "+msg;
        }
    }

    public static interface Callback {
        public String add(int a, int b) throws Exception;
    }

    public static interface CallbackClient {
        public String addResult(int a, int b, int result);
        public String clientMsg(String msg) throws RemoteException;
    }

    public static class CallbackSetup extends TestSetup<Callback> {

        public CallbackClientImpl callbackClientImpl = null;

        public CallbackSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.newService();
            service.loadServiceIDLFromString("KIARA",
                    "namespace * cb "
                            + "service cb { "
                            + "    void add(i32 a, i32 b) "
                            + "    [Callback] string addResult(i32 a, i32 b, i32 result) "
                            + "    [Callback] string clientMsg(string msg) "
                            + "} "
            );

            System.out.printf("Register server functions ....%n");
            CallbackImpl impl = new CallbackImpl();
            service.registerServiceFunction("cb.add", impl, "add");
            System.out.printf("Starting server...%n");

            Server server = context.newServer("0.0.0.0", port, "/service");
            if ("http".equals(transport))
                server.addService("/rpc/cb", protocol, service);
            else if ("tcp".equals(transport))
                server.addService("tcp://0.0.0.0:53212", protocol, service);
            else if ("ws".equals(transport))
                server.addService("ws://0.0.0.0:9090/websocket", protocol, service);
            else
                throw new IllegalArgumentException("Unknown transport "+transport);

            server.addEventListener(new ServerConnectionListener() {

                @Override
                public void onConnectionOpened(ServerConnection connection) {
                    try {
                        MethodBinding<CallbackClient> binder
                                = new MethodBinding<>(CallbackClient.class)
                                .bind("cb.clientMsg", "clientMsg");

                        CallbackClient cc = connection.getServiceInterface(binder);
                        assertEquals("cb.clientMsg: MSG1", cc.clientMsg("MSG1"));
                    } catch (RemoteException ex) {
                        System.err.printf("MSG1 Exception: %s%n", ex);
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        System.err.printf("MSG1 Exception: %s%n", ex);
                        ex.printStackTrace();
                    } catch (SecurityException ex) {
                        System.err.printf("MSG1 Exception: %s%n", ex);
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onConnectionClosed(ServerConnection connection) {

                }
            });

            return server;
        }

        @Override
        protected Callback createClient(Connection connection) throws Exception {
            callbackClientImpl = new CallbackClientImpl();

            connection.registerServiceFunction("cb.addResult", callbackClientImpl, "addResult");
            connection.registerServiceFunction("cb.clientMsg", callbackClientImpl, "clientMsg");

            MethodBinding<Callback> binder
                    = new MethodBinding<>(Callback.class)
                    .bind("cb.add", "add");

            return connection.getServiceInterface(binder);
        }
    }

    private final CallbackSetup cbSetup;
    private Callback cb = null;

    @Before
    public void setUp() throws Exception {
        cb = cbSetup.start(100);
    }

    @After
    public void tearDown() throws Exception {
        cbSetup.shutdown();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Kiara.shutdownGracefully();
    }

    @Parameterized.Parameters
    public static Collection configs() {
        Object[][] data = new Object[][] {
                //{ "http", "jsonrpc" },
                //{ "http", "javaobjectstream" },
                { "tcp", "jsonrpc" },
                { "tcp", "javaobjectstream" },
                { "ws", "jsonrpc" },
                { "ws", "javaobjectstream" }

        };
        return Arrays.asList(data);
    }

    final int PORT = 8080;

    public CallbackTest(String transport, String protocol) {
        cbSetup =  new CallbackSetup(PORT, transport, protocol, "/service");
    }

    @Test
    public void testCallback() throws Exception {
        assertEquals("cb.add: result is 15", cb.add(5, 10));
        assertEquals("cb.add: result is 0", cb.add(-2, 2));
    }

}

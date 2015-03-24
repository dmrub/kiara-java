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
public class ServerPushTest {

    static {
        System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class ServerPushImpl {
        public String add(ServerConnection connection, int a, int b) throws Exception {
            System.out.println("add("+a+","+b+") via connection="+connection);

            ServerPushClient cc = connection.getServiceInterface(ServerPushClient.class);

            String c = cc.addResult(a, b, a + b);

            return "sp.add: "+c;
        }
    }

    public static class ServerPushClientImpl {
        public String addResult(int a, int b, int result) {
            System.out.println("addResult: "+a+" + "+b+" = "+result);
            Assert.assertEquals(a+b, result);
            return "result is "+result;
        }

        public String clientMsg(String msg) {
            System.out.println("sp.clientMsg: " + msg);
            return "sp.clientMsg: "+msg;
        }
    }

    public static interface ServerPush {
        public String add(int a, int b) throws Exception;
    }

    public static interface ServerPushClient {
        public String addResult(int a, int b, int result);
        public String clientMsg(String msg) throws RemoteException;
    }

    public static class ServerPushSetup extends TestSetup<ServerPush> {

        public ServerPushClientImpl callbackClientImpl = null;

        public ServerPushSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();
            service.loadServiceIDLFromString("KIARA",
                    "namespace * sp "
                            + "service sp { "
                            + "    void add(i32 a, i32 b) "
                            + "    [ServerPush] string addResult(i32 a, i32 b, i32 result) "
                            + "    [ServerPush] string clientMsg(string msg) "
                            + "} "
            );

            System.out.printf("Register server functions ....%n");
            ServerPushImpl impl = new ServerPushImpl();
            service.registerServiceFunction("sp.add", impl, "add");
            System.out.printf("Starting server...%n");

            Server server = context.createServer("0.0.0.0", port, "/service");
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
                        MethodBinding<ServerPushClient> binder
                                = new MethodBinding<>(ServerPushClient.class)
                                .bind("sp.clientMsg", "clientMsg")
                                .bind("sp.addResult", "addResult");

                        ServerPushClient cc = connection.getServiceInterface(binder);
                        assertEquals("sp.clientMsg: MSG1", cc.clientMsg("MSG1"));
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
        protected ServerPush createClient(Connection connection) throws Exception {
            callbackClientImpl = new ServerPushClientImpl();

            connection.registerServiceFunction("sp.addResult", callbackClientImpl, "addResult");
            connection.registerServiceFunction("sp.clientMsg", callbackClientImpl, "clientMsg");

            MethodBinding<ServerPush> binder
                    = new MethodBinding<>(ServerPush.class)
                    .bind("sp.add", "add");

            return connection.getServiceInterface(binder);
        }
    }

    private final ServerPushSetup spSetup;
    private ServerPush sp = null;

    @Before
    public void setUp() throws Exception {
        sp = spSetup.start(100);
    }

    @After
    public void tearDown() throws Exception {
        spSetup.shutdown();
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

    public ServerPushTest(String transport, String protocol) {
        spSetup =  new ServerPushSetup(PORT, transport, protocol, "/service");
    }

    @Test
    public void testServerPush() throws Exception {
        assertEquals("sp.add: result is 15", sp.add(5, 10));
        assertEquals("sp.add: result is 0", sp.add(-2, 2));
    }

}

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
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;
import de.dfki.kiara.ServiceConnection;
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
        public String add(ServiceConnection connection, int a, int b) throws Exception {
            System.out.println("add("+a+","+b+") via connection="+connection);

            MethodBinding<CallbackClient> binder
                    = new MethodBinding<>(CallbackClient.class)
                    .bind("calc.addResult", "addResult");

            CallbackClient cc = connection.getServiceInterface(binder);
            String c = cc.addResult(a, b, a+b);
            return "calc.add: "+c;
        }
    }

    public static class CallbackClientImpl {
        public String addResult(int a, int b, int result) {
            System.out.println("addResult: "+a+" + "+b+" = "+result);
            Assert.assertEquals(a+b, result);
            return "result is "+result;
        }
    }

    public static interface Callback {
        public String add(int a, int b) throws Exception;
    }

    public static interface CallbackClient {
        public String addResult(int a, int b, int result);
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
                    "namespace * calc "
                            + "service calc { "
                            + "    void add(i32 a, i32 b) "
                            + "    [Callback] string addResult(i32 a, i32 b, i32 result) "
                            + "} "
            );

            System.out.printf("Register server functions ....%n");
            CallbackImpl impl = new CallbackImpl();
            service.registerServiceFunction("calc.add", impl, "add");
            System.out.printf("Starting server...%n");

            Server server = context.newServer("0.0.0.0", port, "/service");
            if ("http".equals(transport))
                server.addService("/rpc/calc", protocol, service);
            else if ("tcp".equals(transport))
                server.addService("tcp://0.0.0.0:53212", protocol, service);
            else
                throw new IllegalArgumentException("Unknown transport "+transport);
            return server;
        }

        @Override
        protected Callback createClient(Connection connection) throws Exception {
            callbackClientImpl = new CallbackClientImpl();

            connection.registerServiceFunction("calc.addResult", callbackClientImpl, "addResult");

            MethodBinding<Callback> binder
                    = new MethodBinding<>(Callback.class)
                    .bind("calc.add", "add");

            return connection.getServiceInterface(binder);
        }
    }

    private final CallbackSetup calcSetup;
    private Callback calc = null;

    @Before
    public void setUp() throws Exception {
        calc = calcSetup.start(100);
    }

    @After
    public void tearDown() throws Exception {
        calcSetup.shutdown();
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
                //{ "tcp", "javaobjectstream" }
        };
        return Arrays.asList(data);
    }

    final int PORT = 8080;

    public CallbackTest(String transport, String protocol) {
        calcSetup =  new CallbackSetup(PORT, transport, protocol, "/service");
    }

    /**
     * Test of main method, of class CalcTestServer.
     */
    @Test
    public void testCallback() throws Exception {
        assertEquals("calc.add: result is 15", calc.add(5, 10));
        assertEquals("calc.add: result is 0", calc.add(-2, 2));
    }

}

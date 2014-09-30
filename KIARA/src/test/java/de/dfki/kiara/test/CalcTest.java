/*
 * Copyright (C) 2014 Shahzad
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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

/**
 * @author Shahzad, Dmitri Rubinstein
 */
@RunWith(Parameterized.class)
public class CalcTest {


    public static class CalcImpl {

        public Future<Integer> add(ListenableFuture<Integer> a, Future<Integer> b) throws ExecutionException, InterruptedException {
            return Futures.immediateFuture(a.get() + b.get());
        }

        public int add(int a, int b) {
            return a + b;
        }

        public float add(float a, float b) {
            return a + b;
        }

        public int stringToInt32(String s) {
            return Integer.parseInt(s);
        }

        public String int32ToString(int i) {
            return Integer.toString(i);
        }
    }

    public static interface Calc {

        public int add(int a, int b);

        public float addFloat(float a, float b);

        public int stringToInt32(String s);

        public String int32ToString(int i);

        public Integer stringToInt32_CharSequence(CharSequence s);

        public CharSequence int32ToString_Integer(Integer i);
    }

    public static class CalcSetup extends TestSetup<Calc> {

        public CalcSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.newService();
            service.loadServiceIDLFromString("KIARA",
                    "namespace * calc "
                            + "service calc { "
                            + "    i32 add(i32 a, i32 b) "
                            + "    float addf(float a, float b) "
                            + "    i32 stringToInt32(string s) "
                            + "    string int32ToString(i32 i) "
                            + "} "
            );

            System.out.printf("Register server functions ....%n");
            CalcImpl impl = new CalcImpl();
            //service.registerServiceFunction("calc.add", impl, "add", Integer.TYPE, Integer.TYPE);
            service.registerServiceFunction("calc.add", impl, "add", ListenableFuture.class, Future.class);
            service.registerServiceFunction("calc.addf", impl, "add", Float.TYPE, Float.TYPE);
            service.registerServiceFunction("calc.stringToInt32", impl, "stringToInt32");
            service.registerServiceFunction("calc.int32ToString", impl, "int32ToString");
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
        protected Calc createClient(Connection connection) throws Exception {
            MethodBinding<Calc> binder
                    = new MethodBinding<>(Calc.class)
                    .bind("calc.add", "add")
                    .bind("calc.addf", "addFloat")
                    .bind("calc.stringToInt32", "stringToInt32")
                    .bind("calc.int32ToString", "int32ToString")
                    .bind("calc.stringToInt32", "stringToInt32_CharSequence")
                    .bind("calc.int32ToString", "int32ToString_Integer");

            return connection.getServiceInterface(binder);
        }
    }

    private final CalcSetup calcSetup;
    private Calc calc = null;

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

    @Parameters
    public static Collection configs() {
        Object[][] data = new Object[][] {
                { "http", "jsonrpc" },
                { "http", "javaobjectstream" },
                { "tcp", "jsonrpc" },
                { "tcp", "javaobjectstream" }
        };
        return Arrays.asList(data);
    }

    final int PORT = 8080;

    public CalcTest(String transport, String protocol) {
        calcSetup =  new CalcSetup(PORT, transport, protocol, "/service");
    }

    /**
     * Test of main method, of class CalcTestServer.
     */
    @Test
    public void testCalc() throws Exception {
        assertEquals(21 + 32, calc.add(21, 32));
        assertEquals(21.1f + 32.2f, calc.addFloat(21.1f, 32.2f), 0.01f);
        assertEquals(-125, calc.stringToInt32("-125"));
        assertEquals("-42", calc.int32ToString(-42));
        assertEquals(new Integer(521), calc.stringToInt32_CharSequence("521"));
        assertEquals("142", calc.int32ToString_Integer(142));
    }

}

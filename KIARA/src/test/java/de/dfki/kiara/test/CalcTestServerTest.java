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

import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodAlreadyBoundException;
import de.dfki.kiara.Service;

import java.io.IOException;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Shahzad
 */
public class CalcTestServerTest {

    private static Service service = null;

    @BeforeClass
    public static void setUpClass() {
        int port = 8080;
        String protocol = "jsonrpc";

        System.out.printf("Server port: %d\n", port);
        System.out.printf("Protocol: %s\n", protocol);
        try (Context context = Kiara.createContext()) {
            service = context.newService();
            service.loadServiceIDLFromString("KIARA",
                    "namespace * calc "
                            + "service calc { "
                            + "    i32 add(i32 a, i32 b) "
                            + "    float addf(float a, float b) "
                            + "    i32 stringToInt32(string s) "
                            + "    string int32ToString(i32 i) "
                            + "} "
            );

            CalcTestServer.CalcImpl impl = new CalcTestServer.CalcImpl();
            service.registerServiceFunction("calc.add", impl, "add", Integer.TYPE, Integer.TYPE);
            service.registerServiceFunction("calc.addf", impl, "add", Float.TYPE, Float.TYPE);
            service.registerServiceFunction("calc.stringToInt32", impl, "stringToInt32");
            service.registerServiceFunction("calc.int32ToString", impl, "int32ToString");
        } catch (MethodAlreadyBoundException | NoSuchMethodException | SecurityException | IOException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        Kiara.shutdownGracefully();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class CalcTestServer.
     */
    @Test
    public void testCalcAdd() throws Exception {
        assertEquals(42 + 23, service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer("{\"jsonrpc\": \"2.0\", \"method\": \"calc.add\", \"params\": [42, 23], \"id\": 1}", "UTF-8")));
    }

    @Test
    public void testCalcAddFloat() throws Exception {
        assertEquals(21.1 + 32.2,
                (float) service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer("{\"jsonrpc\": \"2.0\", \"method\": \"calc.addf\", \"params\": [21.1, 32.2], \"id\": 2}", "UTF-8")),
                0.01);
    }

    @Test
    public void testCalcStringToInt32() throws Exception {
        assertEquals(45, service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer("{\"jsonrpc\": \"2.0\", \"method\": \"calc.stringToInt32\", \"params\": [\"45\"], \"id\": 3}", "UTF-8")));
    }

    @Test
    public void testCalcInt32ToString() throws Exception {
        assertEquals("132", service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer("{\"jsonrpc\": \"2.0\", \"method\": \"calc.int32ToString\", \"params\": [132], \"id\": 4}", "UTF-8")));
    }
}

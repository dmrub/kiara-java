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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Shahzad
 */
public class StructTestServerTest {

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
                    "namespace * struct_test "
                    + "typedef i64 Integer "
                    + "struct Data { "
                    + " Integer ival, "
                    + "  string  sval "
                    + "} "
                    + "struct Vec3f {"
                    + " float x, "
                    + " float y, "
                    + " float z "
                    + "} "
                    + "struct Quatf {"
                    + " float r, "
                    + " Vec3f v  "
                    + "} "
                    + "struct Location {"
                    + " Vec3f position, "
                    + " Quatf rotation  "
                    + "} "
                    + "exception Exception {"
                    + " i32 code, "
                    + " string message "
                    + "} "
                    + "service StructTest { "
                    + "  Data pack(Integer ival, string sval); "
                    + "  Integer getInteger(Data data); "
                    + "  string getString(Data data); "
                    + "  void setLocation(Location location);"
                    + "  Location getLocation();"
                    + "  void throwException(i32 code, string message) throws (Exception error); "
                    + "} "
            );

            StructTestServer.StructTestImpl structImpl = new StructTestServer.StructTestImpl();
            service.registerServiceFunction("StructTest.pack", structImpl, "pack");
            service.registerServiceFunction("StructTest.getInteger", structImpl, "getInteger");
            service.registerServiceFunction("StructTest.getString", structImpl, "getString");
            service.registerServiceFunction("StructTest.setLocation", structImpl, "setLocation");
            service.registerServiceFunction("StructTest.getLocation", structImpl, "getLocation");
            service.registerServiceFunction("StructTest.throwException", structImpl, "throwException");
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
     * Test of main method, of class StructTestServer.
     */
    @Test
    public void testStructPack() {
        service.DbgSimulateCall(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.pack\", \"params\": [23, \"TEST\"], \"id\": 1}");
    }

    @Test
    public void testStructGetInteger() {
        service.DbgSimulateCall(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getInteger\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 2}");
    }

    @Test
    public void testStructGetString() {
        service.DbgSimulateCall(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getString\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 3}");
    }

    @Test
    public void testStructThrowException() {
        service.DbgSimulateCall(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.throwException\", \"params\": [202, \"NOT FOUND\"], \"id\": 4}");
    }
}

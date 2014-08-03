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

import de.dfki.kiara.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shahzad
 */
public class StructTestServerTest {


    public static class StructTestImpl {

        public static class Data {

            public long ival;
            public String sval;

            Data() {
            }

            public Data(long ival, String sval) {
                this.ival = ival;
                this.sval = sval;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Data))
                    return false;
                Data other = (Data)obj;
                return this.ival == other.ival && this.sval.equals(other.sval);
            }
        }

        public static class Vec3f {

            public float x;
            public float y;
            public float z;

            public Vec3f() {
            }

            public Vec3f(float x, float y, float z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }

        }

        public static class Quatf {

            public float r;
            public Vec3f v;

            public Quatf() {
            }

            public Quatf(float r, Vec3f v) {
                this.r = r;
                this.v = v;
            }
        }

        public static class Location {

            public Vec3f position;
            public Quatf rotation;

            public Location() {
            }

            public Location(Vec3f position, Quatf rotation) {
                this.position = position;
                this.rotation = rotation;
            }
        }

        public Data pack(long ival, String sval) {
            return new Data(ival, sval);
        }

        public long getInteger(Data data) {
            return data.ival;
        }

        public String getString(Data data) {
            return data.sval;
        }

        public Location getLocation() {
            return location;
        }

        private Location location = null;

        public void setLocation(Location location) {
            System.out.printf("Location.position %f %f %f\nLocation.rotation %f %f %f %f\n",
                    location.position.x,
                    location.position.y,
                    location.position.z,
                    location.rotation.r,
                    location.rotation.v.x,
                    location.rotation.v.y,
                    location.rotation.v.z);
            this.location = location;
        }

        public void throwException(int code, String message) throws GenericRemoteException {
            throw new GenericRemoteException(message, code);
        }
    }


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

            StructTestImpl structImpl = new StructTestImpl();
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
    public void testStructPack() throws Exception {
        assertEquals(new StructTestImpl.Data(23, "TEST"),
        service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer("{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.pack\", \"params\": [23, \"TEST\"], \"id\": 1}", "UTF-8")));
    }

    @Test
    public void testStructGetInteger() throws Exception {
        assertEquals(45L,
                service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getInteger\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 2}", "UTF-8")));
    }

    @Test
    public void testStructGetString() throws Exception {
        assertEquals("BLAH",
        service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getString\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 3}", "UTF-8")));
    }

    @Test(expected = Exception.class)
    public void testStructThrowException() throws Exception {
        service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.throwException\", \"params\": [202, \"NOT FOUND\"], \"id\": 4}", "UTF-8"));
    }
}

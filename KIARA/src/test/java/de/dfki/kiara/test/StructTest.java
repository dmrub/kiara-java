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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitri Rubinstein
 */
@RunWith(Parameterized.class)
public class StructTest {


    public static class Data implements Serializable {

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
            if (obj == null || !(obj instanceof Data))
                return false;
            Data other = (Data)obj;
            return ival == other.ival && sval.equals(other.sval);
        }

        @Override
        public String toString() {
            return "Data("+ival+", \""+sval+"\")";
        }
    }

    static boolean equals(double expected, double actual, double delta) {
        if (Double.compare(expected, actual) == 0)
            return true;
        if (!(Math.abs(expected - actual) <= delta))
            return false;
        return true;
    }

    public static class Vec3f implements Serializable {

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

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Vec3f))
                return false;
            Vec3f other = (Vec3f)obj;
            return StructTest.equals(other.x, this.x, 0.01f) &&
                    StructTest.equals(other.y, this.y, 0.01f) &&
                    StructTest.equals(other.z, this.z, 0.01f);
        }

        @Override
        public String toString() {
            return "Vec3f("+x+", "+y+", "+z+")";
        }
    }

    public static class Quatf implements Serializable {

        public float r;
        public Vec3f v;

        public Quatf() {
        }

        public Quatf(float r, Vec3f v) {
            this.r = r;
            this.v = v;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Quatf))
                return false;
            Quatf other = (Quatf)obj;
            return StructTest.equals(other.r, this.r, 0.01f) && v.equals(other.v);
        }

        @Override
        public String toString() {
            return "Quatf("+r+", "+v+")";
        }
    }

    public static class Location implements Serializable {

        public Vec3f position;
        public Quatf rotation;

        public Location() {
        }

        public Location(Vec3f position, Quatf rotation) {
            this.position = position;
            this.rotation = rotation;
        }

        @Override
        public String toString() {
            return "Location("+position+", "+rotation+")";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Location))
                return false;
            Location other = (Location)obj;
            return position.equals(other.position) && rotation.equals(other.rotation);
        }
    }

    public static interface StructTestIface {

        public Data pack(long ival, String sval);

        public long getInteger(Data data);

        public String getString(Data data);

        public Location getLocation();

        public void setLocation(Location location);

        public void throwException(int code, String message) throws GenericRemoteException;
    }


    public static class StructTestImpl {

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

    public static class StructSetup extends TestSetup<StructTestIface> {

        public StructSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.newService();
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

            System.out.printf("Register StructTest.pack ...\n");
            StructTestImpl structImpl = new StructTestImpl();
            service.registerServiceFunction("StructTest.pack", structImpl, "pack");
            service.registerServiceFunction("StructTest.getInteger", structImpl, "getInteger");
            service.registerServiceFunction("StructTest.getString", structImpl, "getString");
            service.registerServiceFunction("StructTest.setLocation", structImpl, "setLocation");
            service.registerServiceFunction("StructTest.getLocation", structImpl, "getLocation");
            service.registerServiceFunction("StructTest.throwException", structImpl, "throwException");
            System.out.printf("Starting server...%n");

            Server server = context.newServer("0.0.0.0", port, "/service");
            if ("http".equals(transport))
                server.addService("/rpc/structTest", protocol, service);
            else if ("tcp".equals(transport))
                server.addService("tcp://0.0.0.0:53212", protocol, service);
            else
                throw new IllegalArgumentException("Unknown transport "+transport);
            return server;
        }

        @Override
        protected StructTestIface createClient(Connection connection) throws Exception {
            MethodBinding<StructTestIface> binder
                    = new MethodBinding<>(StructTestIface.class)
                    .bind("StructTest.pack", "pack")
                    .bind("StructTest.getInteger", "getInteger")
                    .bind("StructTest.getString", "getString")
                    .bind("StructTest.setLocation", "setLocation")
                    .bind("StructTest.getLocation", "getLocation")
                    .bind("StructTest.throwException", "throwException");

            return connection.getServiceInterface(binder);
        }
    }

    private final StructSetup structSetup;
    private StructTestIface structTest = null;

    @Before
    public void setUp() throws Exception {
        structTest = structSetup.start(100);
    }

    @After
    public void tearDown() throws Exception {
        structSetup.shutdown();
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

    public StructTest(String transport, String protocol) {
        structSetup =  new StructSetup(PORT, transport, protocol, "/service");
    }

    /**
     * Test of main method, of class StructTest.
     */
    @Test
    public void testStruct() throws Exception {
        assertEquals(new Data(21, "test21"), structTest.pack(21, "test21"));

        assertEquals(25L, structTest.getInteger(new Data(25, "test25")));

        assertEquals("test72", structTest.getString(new Data(72, "test72")));

        Location location = new Location(new Vec3f(0.5f, 10.5f, 8.0f), new Quatf(0.707107f, new Vec3f(0.0f, 0.0f, 0.70710701f)));
        structTest.setLocation(location);
        Location result = structTest.getLocation();

        assertEquals(location, result);
    }

    @Test(expected = Exception.class)
    public void testStructException() throws Exception {
        structTest.throwException(1234, "error");
    }

}

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

import de.dfki.kiara.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dmitri Rubinstein on 8/5/14.
 */
@RunWith(Parameterized.class)
public class AosTest {

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


    public static class LocationList implements Serializable {

        public List<Location> locations;

        public LocationList() {
        }

        public LocationList(List<Location> locations) {
            this.locations = locations;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof LocationList))
                return false;
            LocationList other = (LocationList)obj;
            return locations.equals(other.locations);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for(Location l : locations){
                sb.append(l).append(' ');
            }
            sb.append(']');
            return sb.toString();
        }

    }


    public static interface GetSetLocations {

        public void setLocations(LocationList locations);

        public LocationList getLocations();
    }


    public static class GetSetLocationsImpl {

        private LocationList locationList = null;

        public void setLocations(LocationList locations) {
            int num = locations.locations.size();
            for (int i = 0; i < num; ++i) {
                System.out.printf("Location.position %f %f %f\nLocation.rotation %f %f %f %f\n",
                        locations.locations.get(i).position.x,
                        locations.locations.get(i).position.y,
                        locations.locations.get(i).position.z,
                        locations.locations.get(i).rotation.r,
                        locations.locations.get(i).rotation.v.x,
                        locations.locations.get(i).rotation.v.y,
                        locations.locations.get(i).rotation.v.z);
            }

            this.locationList = locations;
        }

        public LocationList getLocations() {
            return locationList;
        }
    }

    public static class AosSetup extends TestSetup<GetSetLocations> {

        public AosSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.newService();
            service.loadServiceIDLFromString("KIARA",
                    "namespace * aostest "
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
                            + "struct LocationList { "
                            + " array<Location> locations "
                            + "} "
                            + "service aostest { "
                            + "  void setLocations(LocationList locations); "
                            + "  LocationList getLocations(); "
                            + "} "
            );

            System.out.printf("Register server functions ....%n");
            GetSetLocationsImpl impl = new GetSetLocationsImpl();
            service.registerServiceFunction("aostest.setLocations", impl, "setLocations");
            service.registerServiceFunction("aostest.getLocations", impl, "getLocations");
            System.out.printf("Starting server...%n");

            Server server = context.newServer("0.0.0.0", port, "/service");
            if ("http".equals(transport))
                server.addService("/rpc/aostest", protocol, service);
            else if ("tcp".equals(transport))
                server.addService("tcp://0.0.0.0:53212", protocol, service);
            else
                throw new IllegalArgumentException("Unknown transport "+transport);
            return server;
        }

        @Override
        protected GetSetLocations createClient(Connection connection) throws Exception {
            MethodBinding<GetSetLocations> binder
                    = new MethodBinding<GetSetLocations>(GetSetLocations.class)
                    .bind("aostest.setLocations", "setLocations")
                    .bind("aostest.getLocations", "getLocations");

            return connection.getServiceInterface(binder);
        }
    }

    private final AosSetup aosSetup;
    private GetSetLocations getSetLocations = null;

    @Before
    public void setUp() throws Exception {
        getSetLocations = aosSetup.start(100);
    }

    @After
    public void tearDown() throws Exception {
        aosSetup.shutdown();
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

    private final int PORT = 8080;

    public AosTest(String transport, String protocol) {
        aosSetup =  new AosSetup(PORT, transport, protocol, "/service");
    }

    /**
     * Test of main method, of class AosTestServer.
     */
    @Test
    public void testAos() throws Exception {

        LocationList locations;
        {
            int num = 10;
            List<Location> loclist = new ArrayList<>(num);
            for (int i = 0; i < num; ++i) {
                Location loc = new Location(
                        new Vec3f(i, i, i),
                        new Quatf(0.707107f,
                                new Vec3f(0.0f, 0.0f, 0.70710701f)));
                loclist.add(loc);
            }
            locations = new LocationList(loclist);
        }

        getSetLocations.setLocations(locations);

        assertEquals(locations, getSetLocations.getLocations());
    }

}

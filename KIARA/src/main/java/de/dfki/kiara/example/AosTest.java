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
package de.dfki.kiara.example;

import de.dfki.kiara.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class AosTest {

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

    public static class LocationList {

        public List<Location> locations;

        public LocationList() {
        }

        public LocationList(List<Location> locations) {
            this.locations = locations;
        }

    }

    public static interface GetSetLocations {

        public void setLocations(LocationList locations);

        public LocationList getLocations();
    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://localhost:8080/service";
        }

        System.out.format("Opening connection to %s...\n", uri);

        try (Context context = Kiara.createContext();
                Connection connection = context.connect(uri)) {

            MethodBinding<GetSetLocations> binder
                    = new MethodBinding<>(GetSetLocations.class)
                    .bind("aostest.setLocations", "setLocations")
                    .bind("aostest.getLocations", "getLocations");

            GetSetLocations getSetLocations = connection.getServiceInterface(binder);
            RemoteInterface ri = (RemoteInterface) getSetLocations;
            ConnectionBase c = ri.getConnection();

            /* Send 10 locations to the server, where they will be stored */
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
                getSetLocations.setLocations(new LocationList(loclist));
            }

            /* Receive locations stored on the server, and print them */
            {
                LocationList loclist = getSetLocations.getLocations();
                System.out.println("aostest.getLocations: LocationList {");
                System.out.println("  locations: [");
                for (Location l : loclist.locations) {
                    System.out.format("    position %f %f %f rotation %f %f %f %f\n",
                            l.position.x, l.position.y, l.position.z,
                            l.rotation.r, l.rotation.v.x, l.rotation.v.y, l.rotation.v.z);
                }
                System.out.println("  ]");
                System.out.println("}");
            }

        } finally {
            Kiara.shutdownGracefully();
        }
    }
}

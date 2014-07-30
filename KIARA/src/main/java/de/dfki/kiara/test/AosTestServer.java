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
import de.dfki.kiara.IDLParseException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;
import java.util.List;

/**
 *
 * @author Shahzad
 */
public class AosTestServer {

    public static void main(String args[]) throws Exception {
        int port;
        String protocol;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        if (args.length > 1) {
            protocol = args[1];
        } else {
            protocol = "jsonrpc";
        }
        System.out.printf("Server port: %i\n", port);
        System.out.printf("Protocol: %s\n", protocol);
        Service service = null;
        try (Context context = Kiara.createContext()) {
            service = context.newService();
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
            System.out.printf("Register aostest.setLocations ...\n");
            GetSetLocationsImpl locationsImpl = new GetSetLocationsImpl();
            service.registerServiceFunction("aostest.setLocations", locationsImpl, "setLocations");
            service.registerServiceFunction("aostest.getLocations", locationsImpl, "getLocations");
            System.out.printf("Starting server...\n");
            Server server = context.newServer("0.0.0.0", port, "/service");
            server.addService("/rpc/aostest", protocol, service);
            server.run();
        } catch (IDLParseException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        } finally {
            Kiara.shutdownGracefully();
        }
    }
}

class GetSetLocationsImpl {

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

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
import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.IDLParseException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;

/**
 *
 * @author Shahzad
 */
public class StructTestServer {

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
        System.out.printf("Server port: %d\n", port);
        System.out.printf("Protocol: %s\n", protocol);
        Service service = null;
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

            System.out.printf("Register StructTest.pack ...\n");
            StructTestImpl structImpl = new StructTestImpl();
            service.registerServiceFunction("StructTest.pack", structImpl, "pack");
            service.registerServiceFunction("StructTest.getInteger", structImpl, "getInteger");
            service.registerServiceFunction("StructTest.getString", structImpl, "getString");
            service.registerServiceFunction("StructTest.setLocation", structImpl, "setLocation");
            service.registerServiceFunction("StructTest.getLocation", structImpl, "getLocation");
            service.registerServiceFunction("StructTest.throwException", structImpl, "throwException");

            // Debugging calls start
            service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer("{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.pack\", \"params\": [23, \"TEST\"], \"id\": 1}", "UTF-8"));
            service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                    "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getInteger\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 2}", "UTF-8"));
            service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                    "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getString\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 3}", "UTF-8"));
            service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                    "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.getString\", \"params\": [{\"ival\" : 45, \"sval\" : \"BLAH\"}], \"id\": 3}", "UTF-8"));
            service.dbgSimulateCall(de.dfki.kiara.Util.stringToBuffer(
                    "{\"jsonrpc\": \"2.0\", \"method\": \"StructTest.throwException\", \"params\": [202, \"NOT FOUND\"], \"id\": 4}", "UTF-8"));
            // Debugging calls end

            System.out.printf("Starting server...\n");
            Server server = context.newServer("0.0.0.0", port, "/service");
            server.addService("/rpc/struct", protocol, service);
            server.run();
        } catch (IDLParseException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        } finally {
            Kiara.shutdownGracefully();
        }
    }

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
}

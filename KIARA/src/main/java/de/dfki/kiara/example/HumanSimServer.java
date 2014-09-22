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

import java.util.List;

import de.dfki.kiara.Context;
import de.dfki.kiara.IDLParseException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;

/**
 * Created by Dmitri Rubinstein on 9/11/14.
 */
public class HumanSimServer {

    public static enum LocationType {
        STATIC(0), DYNAMIC(1), AGENT(2);

        private final int value;
        private LocationType(int value) {
            this.value = value;
        }
    }

    public static class KeyValuePair {
        public String key;
        public String value;

        public KeyValuePair() {
        }

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class CustomParameter {
        public List<KeyValuePair> parameter;

        CustomParameter() { }

        CustomParameter(List<KeyValuePair> parameter) {
            this.parameter = parameter;
        }
    }

    public static void main(String args[]) throws Exception {
        int port;
        String protocol;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 21002;
        }
        if (args.length > 1) {
            protocol = args[1];
        } else {
            protocol = "jsonrpc";
        }
        System.out.printf("Server port: %d\n", port);
        System.out.printf("Protocol: %s\n", protocol);

        /* Create new context,
         * it will be automatically closed at the end of
         * the try block
         */
        try (Context context = Kiara.createContext()) {

            /* Create a new service */

            Service service = context.newService();

            /* Add IDL to the service */

            service.loadServiceIDLFromString("KIARA",
                     "namespace * humansim\n"
                    +" typedef i32 LocationID\n"
                    +" typedef i32 AvatarID\n"
                    +"\n"
                    +" enum LocationType {\n"
                    +"    STATIC = 0,\n"
                    +"    DYNAMIC = 1,\n"
                    +"    AGENT = 2\n"
                    +" }\n"
                    +"\n"
                    +" struct KeyValuePair {\n"
                    +"    string key;\n"
                    +"    string value;\n"
                    +" }\n"
                    +"\n"
                    +" struct CustomParameter {\n"
                    +"    array<KeyValuePair> parameter;\n"
                    +" }\n"
                    +"\n"
                    +" service HumanSim {\n"
                    +"\n"
                    +"    LocationID createLocation(string name,\n"
                    +"            float x, float y, float z,\n"
                    +"            float locationReachedDistance,\n"
                    +"            float preferredAvatarOrientation,\n"
                    +"            LocationType type);\n"
                    +"\n"
                    +"    AvatarID createIntelligentAvatar(string avatarName,\n"
                    +"            string implementationType,\n"
                    +"            float x, float y, float z,\n"
                    +"            CustomParameter customParameter);\n"
                    +"}\n");

            System.out.printf("Register HumanSim ...\n");

            /* Create new instance of the implementation class */

            HumanSimImpl humanSimImpl = new HumanSimImpl();

            service.registerServiceFunction("HumanSim.createLocation", humanSimImpl, "createLocation");
            service.registerServiceFunction("HumanSim.createIntelligentAvatar", humanSimImpl, "createIntelligentAvatar");

            /*
             * Create new server and register service
             */

            Server server = context.newServer("0.0.0.0", port, "/service");

            server.addService("/rpc/humansim", protocol, service);

            System.out.printf("Starting server...\n");

            /* Run server */

            server.run();
        } catch (IDLParseException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        } finally {
            // Kiara.shutdownGracefully();
        }
    }

    public static class HumanSimImpl {

        public int createLocation(String name, float x, float y, float z,
                                  float locationReachedDistance,
                                  float preferredAvatarOrientation,
                                  LocationType type) {
            System.out.printf("createLocation(%s,%f,%f,%f,%f,%f,%s)%n", name, x, y, z, locationReachedDistance, preferredAvatarOrientation, type);
            return 10;
        }

        public int createIntelligentAvatar(String avatarName,
                                           String implementationType,
                                           float x,
                                           float y,
                                           float z,
                                           CustomParameter customParameter) {
            System.out.printf("createIntelligentAvatar(%s,%s,%f,%f,%f,%s)%n", avatarName, implementationType, x, y, z, customParameter);
            return 20;
        }
    }

}

/*
 * Copyright (C) 2014 shahzad
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

/**
 *
 * @author shahzad
 */
public class CalcTestServer {

    public static void main(String[] args) throws Exception {
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
                    "namespace * calc "
                    + "service calc { "
                    + "    i32 add(i32 a, i32 b) "
                    + "    float addf(float a, float b) "
                    + "    i32 stringToInt32(string s) "
                    + "    string int32ToString(i32 i) "
                    + "} "
            );

            System.out.printf("Register calc.add ....\n");
            CalcImpl impl = new CalcImpl();
            service.registerServiceFunction("calc.add", impl, "add", Integer.TYPE, Integer.TYPE);
            service.registerServiceFunction("calc.addf", impl, "add", Float.TYPE, Float.TYPE);
            service.registerServiceFunction("calc.stringToInt32", impl, "stringToInt32");
            service.registerServiceFunction("calc.int32ToString", impl, "int32ToString");
            System.out.printf("Starting server...\n");

            // Debugging calls start
            service.DbgSimulateCall(
                    "{\"jsonrpc\": \"2.0\", \"method\": \"calc.add\", \"params\": [42, 23], \"id\": 1}");
            service.DbgSimulateCall(
                         "{\"jsonrpc\": \"2.0\", \"method\": \"calc.addf\", \"params\": [21.1, 32.2], \"id\": 2}");
            service.DbgSimulateCall(
                         "{\"jsonrpc\": \"2.0\", \"method\": \"calc.stringToInt32\", \"params\": [\"45\"], \"id\": 3}");
            service.DbgSimulateCall(
                         "{\"jsonrpc\": \"2.0\", \"method\": \"calc.int32ToString\", \"params\": [132], \"id\": 4}");
            // Debugging calls end

            Server server = context.newServer("0.0.0.0", port, "/service");
            server.addService("/rpc/calc", protocol, service);
            server.run();
        } catch (IDLParseException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        } finally {
            Kiara.shutdownGracefully();
        }
    }

    public static class CalcImpl {

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

}

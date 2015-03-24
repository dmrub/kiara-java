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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Context;
import de.dfki.kiara.IDLParseException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


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
            service = context.createService();
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
            //service.registerServiceFunction("calc.add", impl, "add", Integer.TYPE, Integer.TYPE);
            service.registerServiceFunction("calc.add", impl, "add", ListenableFuture.class, Future.class);
            service.registerServiceFunction("calc.addf", impl, "add", Float.TYPE, Float.TYPE);
            service.registerServiceFunction("calc.stringToInt32", impl, "stringToInt32");
            service.registerServiceFunction("calc.int32ToString", impl, "int32ToString");
            System.out.printf("Starting server...\n");

            Server server = context.createServer("0.0.0.0", port, "/service");
            server.addService("/rpc/calc", protocol, service);
            server.run();
        } catch (IDLParseException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        } finally {
            //Kiara.shutdownGracefully();
        }
    }

    public static class CalcImpl {

        public Future<Integer> add(ListenableFuture<Integer> a, Future<Integer> b) throws ExecutionException, InterruptedException {
            return Futures.immediateFuture(a.get() + b.get());
        }

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

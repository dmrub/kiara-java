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
package dfki.sb.zerociceproject;

/**
 *
 * @author Shahzad
 */
public class IceJavaServer {

    public static void startServer() {
        int status = 0;
        Ice.Communicator ic = null;
        try {
            ic = Ice.Util.initialize();
            Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints(
                    "BenchmarkAdapter", "tcp -p 10000");
            adapter.add(new BenchmarkI(), ic.stringToIdentity("Benchmark"));
            System.out.println("Starting Ice server...");
            adapter.activate();
            System.out.println("Started Ice server...");
            ic.waitForShutdown();
        } catch (Ice.LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        }
        if (ic != null) {
            // Clean up
            //
            try {
                ic.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                status = 1;
            }
        }
        System.exit(status);

    }

    public static void main(String[] args) {
        startServer();
    }
}

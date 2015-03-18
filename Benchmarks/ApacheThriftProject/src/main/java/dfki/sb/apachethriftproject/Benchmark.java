/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package dfki.sb.apachethriftproject;

import java.io.IOException;
import java.net.Socket;
import org.apache.thrift.server.TServer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Benchmark {

    public static void main(String[] args) throws Exception {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/nologging.properties");
        System.setProperty("java.util.logging.config.file", "");
        final int port = 8090;

        final TServer server = ThriftJavaServer.createServer(port);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Starting server...");
                server.serve();
            }
        });

        thread.start();

        boolean connected = false;
        while (!connected) {
            try {
                Socket s = new Socket("localhost", port);
                connected = s.isConnected();
                s.close();
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }

        System.out.println("Run measurements...");

        ThriftJavaClient.runClient(port);

        System.out.println("Shutdown");

        server.stop();
        thread.join();
    }
}

/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
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
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Benchmark {

    public static void main(String[] args) throws Exception {
        final int port = 8090;

        final Context c1 = Kiara.createContext();

        Server server = BenchmarkServer.runServer(c1, port, "jsonrpc");

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

        final Context c2 = Kiara.createContext();
        BenchmarkClient.runClient(c2, "http://localhost:" + port + "/service");

        System.out.println("Shutdown");

        server.close();
        c1.close();
        c2.close();
        Kiara.shutdownGracefully();
    }
}

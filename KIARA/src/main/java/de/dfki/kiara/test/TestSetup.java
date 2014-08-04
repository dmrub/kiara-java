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

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

/**
 * Created by Dmitri Rubinstein on 8/4/14.
 */
public abstract class TestSetup<CLIENT_INTERFACE> {
    protected final int port;
    protected final String transport;
    protected final String protocol;
    protected final String configPath;
    protected volatile Server server;
    protected Context clientCtx;
    protected Context serverCtx;

    public TestSetup(int port, String transport, String protocol, String configPath) {
        this.port = port;
        this.transport = transport;
        this.protocol = protocol;
        this.configPath = configPath;
        this.server = null;
        this.clientCtx = null;
        this.serverCtx = null;
    }

    protected abstract Server createServer(Context serverCtx, int port, String transport, String protocol, String configPath) throws Exception;

    protected abstract CLIENT_INTERFACE createClient(Connection connection) throws Exception;

    public boolean checkConnection(int timeout) {
        boolean connected = false;
        long currentTime, startTime;
        currentTime = startTime = System.currentTimeMillis();
        while (!connected && ((currentTime-startTime) < timeout)) try {
            Socket s = new Socket(InetAddress.getLocalHost(), port);
            connected = s.isConnected();
            s.close();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return connected;
    }

    public CLIENT_INTERFACE start(int timeout) throws Exception {
        serverCtx = Kiara.createContext();
        server = createServer(serverCtx, port, transport, protocol, configPath);
        System.out.printf("Starting server...%n");
        server.run();

        if (!checkConnection(timeout))
            throw new IOException("Could not start server");

        clientCtx = Kiara.createContext();

        final URI uri = new URI("http://localhost:"+port+"/"+configPath).normalize();
        System.out.printf("Opening connection to %s...%n", uri);
        Connection connection = clientCtx.openConnection(uri.toString());

        return createClient(connection);
    }

    void shutdown() throws Exception {
        System.out.println("Shutdown");
        server.close();
        clientCtx.close();
        serverCtx.close();
    }
}

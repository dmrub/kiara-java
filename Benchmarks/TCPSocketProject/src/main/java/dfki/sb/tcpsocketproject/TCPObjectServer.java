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
package dfki.sb.tcpsocketproject;

import dfki.sb.tcpsocketproject.model.MarketData;
import dfki.sb.tcpsocketproject.model.QuoteRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Shahzad
 */
public class TCPObjectServer {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        try {
            ServerSocket clientConnect = new ServerSocket(8081);
            System.out.println("Starting server: waiting for client on port 8081");
            serveClient(clientConnect);
            if (args != null && args.length > 0 && args[0].equalsIgnoreCase("infinite")) {
                while (true) {
                    System.out.println("waiting for next client on port 8081");
                    serveClient(clientConnect);
                }
            }
            clientConnect.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serveClient(ServerSocket clientConnect) throws IOException, ClassNotFoundException {
        Socket client = clientConnect.accept(); // blocks
        System.out.println("Client connected ....");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()))) {
            oos.flush();
            Object object;
            while (true) {
                object = ois.readObject();
                if (object instanceof MarketData) {
                    handleMarketData(ois, oos, (MarketData) object);
                } else if (object instanceof QuoteRequest) {
                    handleQuoteRequest(ois, oos, (QuoteRequest) object);
                } else {
                    break;
                }
            }
        }
    }

    private static void handleMarketData(ObjectInputStream ois, ObjectOutputStream oos, MarketData marketData) throws IOException, ClassNotFoundException {
        marketData.setIsEcho(true);
        oos.writeObject(marketData);
        oos.flush();
    }

    public static void handleQuoteRequest(ObjectInputStream ois, ObjectOutputStream oos, QuoteRequest returnRequest) throws IOException, ClassNotFoundException {
        returnRequest.setIsEcho(true);
        oos.writeObject(returnRequest);
        oos.flush();
    }
}

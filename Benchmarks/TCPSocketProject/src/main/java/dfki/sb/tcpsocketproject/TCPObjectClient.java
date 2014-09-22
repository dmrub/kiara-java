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

import dfki.sb.tcpsocketproject.model.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Shahzad
 */
public class TCPObjectClient {

    public static void main(String args[]) {
        try {
            Socket sender = new Socket("localhost", 8081);
            if (sender.isConnected()) {
                int numMessages = 10000;
                long startTime;
                long finishTime;
                try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(sender.getOutputStream()))) {
                    oos.flush();
                    // if i open both streams in same try block it just blocks the streams.
                    try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(sender.getInputStream()))) {
                        sendMessages(20000, oos, ois);
                        startTime = System.currentTimeMillis();
                        sendMessages(numMessages, oos, ois);
                        finishTime = System.currentTimeMillis();
                        oos.writeObject(null);
                        oos.flush();
                    }
                }
                long difference = finishTime - startTime;
                difference = difference * 1000;
                double latency = (double) difference / (numMessages * 2.0);
                System.out.println(String.format("\n\nAverage latency in microseconds %.3f using ObjectStream\n\n\n", latency));
                System.out.println("Finished");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void sendMessages(int numberOfMessages, ObjectOutputStream oos, ObjectInputStream ois) throws ClassNotFoundException, IOException {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                oos.writeObject(Util.createQuoteRequestData());
                oos.flush();
                ois.readObject();
            } else {
                oos.writeObject(Util.createMarketData());
                oos.flush();
                ois.readObject();
            }
        }
    }
}

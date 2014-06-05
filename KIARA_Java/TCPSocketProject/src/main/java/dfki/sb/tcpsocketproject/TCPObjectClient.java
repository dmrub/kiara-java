/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
                ObjectInputStream ois;
                long startTime;
                long finishTime;
                try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(sender.getOutputStream()))) {
                    oos.flush();
                    ois = new ObjectInputStream(new BufferedInputStream(sender.getInputStream()));
                    preprationMethod(oos, ois);
                    startTime = System.currentTimeMillis();
                    for (int i = 0; i < numMessages; i++) {
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
                    finishTime = System.currentTimeMillis();
                    oos.writeObject(null);
                    oos.flush();
                }
                ois.close();
                long difference = finishTime - startTime;
                difference = difference * 1000;
                double latency = (double) difference / (numMessages * 2.0);
                System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds using ObjectStream\n\n\n");
                System.out.println("Finished");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void preprationMethod(ObjectOutputStream oos, ObjectInputStream ois) throws ClassNotFoundException, IOException {
        for (int i = 0; i < 20000; i++) {
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

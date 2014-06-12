/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            System.out.println("Server started: waiting for clienr on port 8081");
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
                clientConnect.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

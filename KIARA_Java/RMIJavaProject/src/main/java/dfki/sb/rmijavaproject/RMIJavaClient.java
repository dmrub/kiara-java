/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.rmijavaproject;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Administrator
 */
public class RMIJavaClient {
    public static void main(String[] args) throws RemoteException, NotBoundException{
        int numMessages = 10000;
        Registry reg = LocateRegistry.getRegistry("127.0.0.1", 9989);
        System.out.println("Connecting to server.");
        RMIInterface client = (RMIInterface) reg.lookup("server");   
        sendMessages(20000,client);
        long startTime = System.currentTimeMillis();
        sendMessages(numMessages, client);
        long finishTime = System.currentTimeMillis();
        long difference = finishTime - startTime; 
        difference = difference * 1000;
        double latency = (double) difference / (numMessages * 2.0);
        System.out.println("\n\nAverage latency is " + String.format("%.3f",latency) + " microseconds\n\n\n");    
        System.out.println("Finished");

    }

    private static void sendMessages(int numberOfMessages,RMIInterface client) throws RemoteException {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                client.sendQuoteRequest(Util.createQuoteRequestData());
            } else {
                client.sendMarketData(Util.createMarketData());
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.rmijavaproject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shahzad
 */
public class RMIJavaServer extends UnicastRemoteObject implements RMIInterface {

    int messageCounter = 0;

    public RMIJavaServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(9989);
            reg.rebind("server", new RMIJavaServer());
            System.out.println("Starting server on port 9989.");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIJavaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public MarketData sendMarketData(MarketData marketData) throws RemoteException {
        int counter = messageCounter;
        ++messageCounter;
        MarketData returnData = marketData;
        returnData.isEcho = true;
        return returnData;
    }

    @Override
    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) throws RemoteException {
        int counter = messageCounter;
        ++messageCounter;

        QuoteRequest returnRequest = quoteRequest;
        returnRequest.isEcho = true;
        return returnRequest;
    }
}

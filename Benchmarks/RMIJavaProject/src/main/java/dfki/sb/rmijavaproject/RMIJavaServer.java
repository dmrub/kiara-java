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

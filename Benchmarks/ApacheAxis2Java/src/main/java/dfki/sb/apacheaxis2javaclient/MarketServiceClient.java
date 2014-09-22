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

package dfki.sb.apacheaxis2javaclient;

import dfki.sb.MarketDataServiceStub;
import dfki.sb.MarketDataServiceStub.SendMarketData;
import dfki.sb.MarketDataServiceStub.SendQuoteRequest;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;

/**
 *
 * @author Administrator
 */
public class MarketServiceClient {
    public static void main(String[] args) {
        try {
            int numMessages = 1000;
            MarketDataServiceStub stub = new MarketDataServiceStub();
            MarketDataServiceStub.SendQuoteRequest quoteData = new MarketDataServiceStub.SendQuoteRequest();
            MarketDataServiceStub.SendMarketData marketData = new MarketDataServiceStub.SendMarketData();                        
            sendMessages(1000,quoteData, stub, marketData);
            long startTime = System.currentTimeMillis();
            sendMessages(numMessages, quoteData, stub, marketData);
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            double latency = (double) difference / (numMessages * 2.0);
            System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n\n");
            System.out.println("Finished");                                    
        } catch (AxisFault ex) {
            Logger.getLogger(MarketServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(MarketServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void sendMessages(int numberOfMessages,SendQuoteRequest quoteData, MarketDataServiceStub stub, SendMarketData marketData) throws RemoteException {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                quoteData.setQuoteRequest(Util.createQuoteRequestData());
                stub.sendQuoteRequest(quoteData);
            } else {
                marketData.setMarketData(Util.createMarketData());
                stub.sendMarketData(marketData);
            }
        }
    }
}

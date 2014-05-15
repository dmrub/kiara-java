/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.apacheaxis2javaclient;

import dfki.sb.MarketDataServiceStub;
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
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < numMessages; i++) {
                // Send 10 MarketDatas for each QuoteRequest
                if (i % 10 == 5) {
                    quoteData.setQuoteRequest(Util.createQuoteRequestData());            
                    stub.sendQuoteRequest(quoteData);                                                            
                } else {
                    marketData.setMarketData(Util.createMarketData());                                  
                    stub.sendMarketData(marketData);                                                            
                }
            }
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            double latency = (double) difference / (numMessages * 2.0);
            System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " milli seconds\n\n\n");
            System.out.println("Finished");                                    
        } catch (AxisFault ex) {
            Logger.getLogger(MarketServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(MarketServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

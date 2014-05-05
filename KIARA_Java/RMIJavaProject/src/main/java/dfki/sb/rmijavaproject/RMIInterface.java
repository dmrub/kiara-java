/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.rmijavaproject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Shahzad
 */
public interface RMIInterface extends Remote{
    public MarketData sendMarketData(MarketData marketData) throws RemoteException;

    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) throws RemoteException;    
}

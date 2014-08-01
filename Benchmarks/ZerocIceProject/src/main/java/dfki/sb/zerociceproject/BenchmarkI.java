/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.zerociceproject;

import Ice.Current;
import dfki.sb.zerociceproject.Main.MarketData;
import dfki.sb.zerociceproject.Main.QuoteRequest;

/**
 *
 * @author Shahzad
 */
public class BenchmarkI extends dfki.sb.zerociceproject.Main._BenchmarkDisp {

    int msgCounter = 0;

    @Override
    public MarketData sendMarketData(MarketData marketData, Current __current) {
        int counter = msgCounter;
        msgCounter++;

        MarketData returnMarketData = marketData;
        returnMarketData.isEcho = true;
        return returnMarketData;
    }

    @Override
    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequestArg, Current __current) {
        int counter = msgCounter;
        msgCounter++;

        QuoteRequest returnQuoteRequest = quoteRequestArg;
        returnQuoteRequest.isEcho = true;
        return returnQuoteRequest;
    }

}

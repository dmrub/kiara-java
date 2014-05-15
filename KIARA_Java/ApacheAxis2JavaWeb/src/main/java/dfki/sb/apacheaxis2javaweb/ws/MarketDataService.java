/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.apacheaxis2javaweb.ws;

import dfki.sb.apacheaxis2javaweb.bean.QuoteRequest;
import dfki.sb.apacheaxis2javaweb.bean.MarketData;

/**
 *
 * @author Administrator
 */
public class MarketDataService {

    private int messageCounter = 0;

    public MarketData sendMarketData(MarketData marketData) {
        int counter = messageCounter;
        ++messageCounter;
        MarketData returnData = marketData;
        returnData.setIsEcho(true);
        return returnData;
    }

    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) {
        int counter = messageCounter;
        ++messageCounter;
        QuoteRequest returnRequest = quoteRequest;
        returnRequest.setIsEcho(true);
        return returnRequest;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.apachethriftproject;

import benchmark.Benchmark;
import benchmark.MarketData;
import benchmark.QuoteRequest;
import org.apache.thrift.TException;

/**
 *
 * @author Shahzad
 */
public class BenchmarkHandler implements Benchmark.Iface {

    int messageCounter = 0;

    public BenchmarkHandler() {
    }

    @Override
    public MarketData sendMarketData(MarketData marketData) throws TException {
        int counter = messageCounter;
        ++messageCounter;
        MarketData returnData = marketData;
        returnData.isEcho = true;
        return returnData;
    }

    @Override
    public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) throws TException {
        int counter = messageCounter;
        ++messageCounter;

        QuoteRequest returnRequest = quoteRequest;
        returnRequest.isEcho = true;
        return returnRequest;
    }

}

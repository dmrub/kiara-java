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

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

package dfki.sb.zeroiceclient;

import dfki.sb.zerociceproject.Main.*;
import dfki.sb.zerociceproject.Util;


/**
 *
 * @author Shahzad
 */
public class IceJavaClient {
    public static void main(String[] args) {
        int numMessages = 1000;
        String host = "localhost";
        long startTime = 0;
        Ice.Communicator ic = null;
        try {
            ic = Ice.Util.initialize(new String[]{});
            Ice.ObjectPrx base = ic.stringToProxy("Benchmark:tcp -p 10000 -h "+host);
            BenchmarkPrx benchmark = BenchmarkPrxHelper.checkedCast(base);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < numMessages; i++) {
                // Send 10 MarketDatas for each QuoteRequest
                if (i % 10 == 5) {
                    benchmark.sendQuoteRequest(Util.createQuoteRequestData());
                } else {
                    benchmark.sendMarketData(Util.createMarketData());
                }
            }
        } catch (Ice.LocalException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        long finishTime = System.currentTimeMillis();        
        long difference = finishTime - startTime;
        difference = difference * 1000;
        if (ic != null) {
            // Clean up
            //
            try {
                ic.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        double latency = (double) difference / (numMessages * 2.0);
        System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n");
        System.out.println("\t\tFinished");
    }
}

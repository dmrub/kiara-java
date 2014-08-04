/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
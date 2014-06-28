/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.zerociceproject;

import dfki.sb.zerociceproject.Main.*;


/**
 *
 * @author Shahzad
 */
public class IceJavaClient {
    public static void main(String[] args) {
        int numMessages = 10000;
        String host = "localhost";
        long startTime = 0;
        Ice.Communicator ic = null;
        try {
            ic = Ice.Util.initialize(new String[]{});
            Ice.ObjectPrx base = ic.stringToProxy("Benchmark:tcp -p 10000 -h "+host);
            BenchmarkPrx benchmark = BenchmarkPrxHelper.checkedCast(base);
            sendMessages(20000,benchmark);
            startTime = System.currentTimeMillis();
            sendMessages(numMessages, benchmark);
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
        System.out.println(String.format("\n\nAverage latency in microseconds %.3f\n\n\n", latency));
        System.out.println("\t\tFinished");
    }

    private static void sendMessages(int numberOfMessages,BenchmarkPrx benchmark) {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                benchmark.sendQuoteRequest(Util.createQuoteRequestData());
            } else {
                benchmark.sendMarketData(Util.createMarketData());
            }
        }
    }
}

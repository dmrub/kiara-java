/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.apachethriftclient;

import benchmark.Benchmark;
import dfki.sb.apachethriftproject.Util;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author Shahzad
 */
public class ThriftJavaClient {

    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                System.out.println("Usage: Client <num-messages> <host>");
                System.exit(0);
            }
            int numMessages = Integer.parseInt(args[1]);
            String host = args[2];
            TTransport transport = new TSocket(host, 9090);
            TProtocol protocol = new TBinaryProtocol(transport);
            Benchmark.Client client = new Benchmark.Client(protocol);
            transport.open();
            long startTime = System.nanoTime();
            for (int i = 0; i < numMessages; i++) {
                // Send 10 MarketDatas for each QuoteRequest
                if (i % 10 == 5) {
                    client.sendQuoteRequest(Util.createQuoteRequestData());
                } else {
                    client.sendMarketData(Util.createMarketData());
                }
            }
            long finishTime = System.nanoTime();
            long difference = finishTime - startTime;
            transport.close();
            double latency = (double) difference / (numMessages * 2.0);
            System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " nano seconds\n\n\n");
            System.out.println("Finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

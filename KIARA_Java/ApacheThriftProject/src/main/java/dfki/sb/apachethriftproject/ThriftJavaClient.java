/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.apachethriftproject;

import benchmark.Benchmark;
import org.apache.thrift.TException;
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
            int numMessages = 10000;
            String host = "localhost";
            TTransport transport = new TSocket(host, 9090);
            TProtocol protocol = new TBinaryProtocol(transport);
            Benchmark.Client client = new Benchmark.Client(protocol);
            transport.open();
            sendMessages(20000,client);
            long startTime = System.currentTimeMillis();
            sendMessages(numMessages, client);
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            transport.close();
            double latency = (double) difference / (numMessages * 2.0);
            System.out.println(String.format("\n\nAverage latency in microseconds %.3f\n\n\n", latency));
            System.out.println("Finished");
        } catch (NumberFormatException | TException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessages(int numberOfMessages,Benchmark.Client client) throws TException {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                client.sendQuoteRequest(Util.createQuoteRequestData());
            } else {
                client.sendMarketData(Util.createMarketData());
            }
        }
    }
}

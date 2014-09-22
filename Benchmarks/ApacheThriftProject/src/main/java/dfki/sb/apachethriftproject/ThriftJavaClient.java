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

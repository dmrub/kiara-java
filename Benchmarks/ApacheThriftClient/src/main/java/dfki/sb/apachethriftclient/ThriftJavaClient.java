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
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < numMessages; i++) {
                // Send 10 MarketDatas for each QuoteRequest
                if (i % 10 == 5) {
                    client.sendQuoteRequest(Util.createQuoteRequestData());
                } else {
                    client.sendMarketData(Util.createMarketData());
                }
            }
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            transport.close();
            double latency = (double) difference / (numMessages * 2.0);
            System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n\n");
            System.out.println("Finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

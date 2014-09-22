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
package de.dfki.kiara.test;

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteInterface;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class BenchmarkClient {

    public static interface Benchmark {

        public MarketData sendMarketData(MarketData marketData);

        public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest);
    }

    private static void sendMessages(int numberOfMessages, Benchmark client) {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                QuoteRequest qr = Util.createQuoteRequestData();
                qr.counter = i;
                qr.isEcho = false;
                client.sendQuoteRequest(qr);
            } else {
                MarketData md = Util.createMarketData();
                md.counter = i;
                md.isEcho = false;
                client.sendMarketData(md);
            }
        }
    }

    public static void runClient(Context context, String uri) throws Exception {
        try (Connection connection = context.openConnection(uri)) {

            MethodBinding<Benchmark> binder
                    = new MethodBinding<>(Benchmark.class)
                    .bind("benchmark.sendMarketData", "sendMarketData")
                    .bind("benchmark.sendQuoteRequest", "sendQuoteRequest");

            Benchmark benchmark = connection.generateClientFunctions(binder);
            RemoteInterface ri = (RemoteInterface) benchmark;
            Connection c = ri.getConnection();

            final int numMessages = 10000;

            sendMessages(20000, benchmark);
            long startTime = System.currentTimeMillis();
            sendMessages(numMessages, benchmark);
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            double latency = (double) difference / (numMessages * 2.0);

            System.out.printf("%n%nAverage latency in microseconds %.3f%n%n%n", latency);
            System.out.println("Finished");
        }
    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://localhost:8080/service";
        }

        System.out.format("Opening connection to %s...\n", uri);

        try (Context context = Kiara.createContext()) {
            runClient(context, uri);
        } finally {
            Kiara.shutdownGracefully();
        }
    }
}

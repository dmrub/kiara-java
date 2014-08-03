/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.test;

import de.dfki.kiara.Context;
import de.dfki.kiara.IDLParseException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;

/**
 *
 * @author Shahzad
 */
public class BenchmarkServer {

    public static class BenchmarkImpl {

        int messageCounter = 0;

        public MarketData sendMarketData(MarketData marketData) {
            int counter = messageCounter;
            ++messageCounter;
            MarketData returnData = marketData;
            returnData.isEcho = true;
            return returnData;
        }

        public QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) {
            int counter = messageCounter;
            ++messageCounter;

            QuoteRequest returnRequest = quoteRequest;
            returnRequest.isEcho = true;
            return returnRequest;
        }
    }

    public static Server runServer(Context context, int port, String protocol) throws Exception {

        /* Create a new service */
        Service service = context.newService();

        /* Add IDL to the service */
        service.loadServiceIDLFromString("KIARA",
                "namespace * benchmark "
                + " "
                + "struct MarketDataEntry { "
                + "u32 mdUpdateAction; "
                + "u32 mdPriceLevel; "
                + "double        mdEntryType; "
                + "u32 openCloseSettleFlag; "
                + "u32 securityIDSource; "
                + "u32 securityID; "
                + "u32 rptSeq; "
                + "double        mdEntryPx; "
                + "u32 mdEntryTime; "
                + "u32          mdEntrySize; " // in original i32
                + "u32 numberOfOrders; "
                + "double        tradingSessionID; "
                + "double        netChgPrevDay; "
                + "u32 tradeVolume; "
                + "double        tradeCondition; "
                + "double        tickDirection; "
                + "double        quoteCondition; "
                + "u32 aggressorSide; "
                + "double        matchEventIndicator; "
                + ""
                + "double dummy1; "
                + "i32 dummy2; "
                + "} "
                + ""
                + "struct MarketData { "
                + "boolean    isEcho; "
                + "u32        counter; "
                + "u32        securityID; "
                + "double    applVersionID; "
                + "double    messageType; "
                + "double    senderCompID; "
                + "u32       msgSeqNum; "
                + "u32       sendingTime; "
                + "u32       tradeDate; "
                + "array<MarketDataEntry>  mdEntries; "
                + "} "
                + ""
                + "struct RelatedSym { "
                + "double    symbol; "
                + "u64       orderQuantity; "
                + "u32    side; "
                + "u64    transactTime; "
                + "u32    quoteType; "
                + "u32      securityID; "
                + "u32      securityIDSource; "
                + "double dummy1; "
                + "i32 dummy2; "
                + "} "
                + "struct QuoteRequest { "
                + "boolean            isEcho; "
                + "u32      counter; "
                + "u32      securityID; "
                + "double             applVersionID; "
                + "double             messageType; "
                + "double             senderCompID; "
                + "u32      msgSeqNum; "
                + "u32      sendingTime; "
                + "double             quoteReqID; "
                + "array<RelatedSym>        related; "
                + "} "
                + ""
                + "service benchmark { "
                + "  MarketData sendMarketData(MarketData marketData); "
                + "  QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest); "
                + "} "
        );
        System.out.printf("Register benchmark.sendMarketData ...%n");

        /* Create new instance of the implementation class */
        BenchmarkImpl benchmarkImpl = new BenchmarkImpl();

        /* Register methods of the instance with the sepcified IDL service methods
         *
         * service.registerServiceFunction(String idlMethodName, Object serviceImpl,
         *                                 String serviceMethodName)
         *
         * service       - valid instance of the Service class
         * idlMethodName - name of the remote service method specified in the IDL.
         * serviceImpl   - arbitrary Java object that implements IDL service method
         * serviceMethodName - name of the Java object method that implements IDL service method
         */
        service.registerServiceFunction("benchmark.sendMarketData", benchmarkImpl, "sendMarketData");
        service.registerServiceFunction("benchmark.sendQuoteRequest", benchmarkImpl, "sendQuoteRequest");

        /*
         * Create new server and register service
         */
        Server server = context.newServer("0.0.0.0", port, "/service");

        //server.addService("/rpc/benchmark", protocol, service);
        server.addService("tcp://0.0.0.0:53212", protocol, service);

        System.out.printf("Starting server...%n");

        /* Run server */
        server.run();
        return server;
    }

    public static void main(String args[]) throws Exception {
        int port;
        String protocol;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        if (args.length > 1) {
            protocol = args[1];
        } else {
            protocol = "jsonrpc";
        }
        System.out.printf("Server port: %d%n", port);
        System.out.printf("Protocol: %s%n", protocol);

        /* Create new context,
         * it will be automatically closed at the end of
         * the try block
         */
        try (Context context = Kiara.createContext()) {
            runServer(context, port, protocol);
        } catch (IDLParseException e) {
            System.out.printf("Error: could not parse IDL: %s", e.getMessage());
            System.exit(1);
        } finally {
            // Kiara.shutdownGracefully();
        }
    }

}

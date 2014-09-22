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

package dfki.sb.rmijavaproject;

import java.util.ArrayList;

/**
 *
 * @author Shahzad
 */
public class Util {
public static MarketData createMarketData() {
        MarketData data = new MarketData();
        data.counter = 0;
        data.applVersionID = 1.0;
        data.messageType = 100;
        data.senderCompID = 121213.0;
        data.msgSeqNum = 4;
        data.sendingTime = 00162635;
        data.tradeDate = 20100422;
        data.mdEntries = new ArrayList<>();
        data.mdEntries.add(createMarketEntryData());
        data.mdEntries.add(createMarketEntryData());
        data.mdEntries.add(createMarketEntryData());
        return data;
    }
    private static MarketDataEntry createMarketEntryData() {
        MarketDataEntry entry = new MarketDataEntry();
        entry.mdUpdateAction = 1;
        entry.mdPriceLevel = 2;
        entry.mdEntryType = 7;
        entry.openCloseSettleFlag = 3;
        entry.securityID = 99;
        entry.securityIDSource = 9;
        entry.rptSeq = 2;
        entry.mdEntryPx = 100.0;
        entry.mdEntryTime = 12345;
        entry.mdEntrySize = 50;
        entry.numberOfOrders = 10;
        entry.tradingSessionID = 2;
        entry.netChgPrevDay = 10.0;
        entry.tradeVolume = 30;
        entry.tradeCondition = 100.0;//double('W');
        entry.tickDirection = 0;
        entry.quoteCondition = 67;//double('C');
        entry.aggressorSide = 2;
        entry.matchEventIndicator = 1;//double('1');
        entry.dummy1 = 1;
        entry.dummy2 = 2;
        return entry;
    }

    public static QuoteRequest createQuoteRequestData() {
        QuoteRequest req = new QuoteRequest();
        req.securityID = 2112;
        req.applVersionID = 1.0;
        req.messageType = 100;
        req.senderCompID = 7881;
        req.msgSeqNum = 4;
        req.sendingTime = 00162635;
        req.quoteReqID = 78;//double('R');
        req.isEcho = false;
        req.counter = 0;
        req.related = new ArrayList<>();
        req.related.add(createRelatedSymData());
        req.related.add(createRelatedSymData());
        req.related.add(createRelatedSymData());
        return req;
    }

    private static RelatedSym createRelatedSymData() {
        RelatedSym sym = new RelatedSym();
        sym.symbol = 321.0;
        sym.orderQuantity = 25;
        sym.side = 1;
        sym.transactTime = 00162635;
        sym.quoteType = 1;
        sym.securityID = 99;
        sym.securityIDSource = 9;
        sym.dummy1 = 1;
        sym.dummy2 = 2;
        return sym;
    }    
}

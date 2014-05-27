package dfki.sb.rabbitmqjava.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Shahzad
 */
public class Util {
public static MarketData createMarketData() {
        MarketData data = new MarketData();
        data.setCounter(0);
        data.setApplVersionID(1.0);
        data.setMessageType(100);
        data.setSenderCompID(121213.0);
        data.setMsgSeqNum(4);
        data.setSendingTime(00162635);
        data.setTradeDate(20100422);
        data.setMdEntries(new ArrayList<MarketDataEntry>());
        data.getMdEntries().add(createMarketEntryData());
        data.getMdEntries().add(createMarketEntryData());
        data.getMdEntries().add(createMarketEntryData());
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
        req.setSecurityID(2112);
        req.setApplVersionID(1.0);
        req.setMessageType(100);
        req.setSenderCompID(7881);
        req.setMsgSeqNum(4);
        req.setSendingTime(00162635);
        req.setQuoteReqID(78);//double('R');
        req.setIsEcho(false);
        req.setCounter(0);
        req.setRelated(new ArrayList<RelatedSym>());
        req.getRelated().add(createRelatedSymData());
        req.getRelated().add(createRelatedSymData());
        req.getRelated().add(createRelatedSymData());
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

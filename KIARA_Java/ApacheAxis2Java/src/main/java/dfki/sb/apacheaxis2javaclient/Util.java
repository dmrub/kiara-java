/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.apacheaxis2javaclient;

import dfki.sb.MarketDataServiceStub;
import dfki.sb.MarketDataServiceStub;

/**
 *
 * @author Shahzad
 */
public class Util {
public static MarketDataServiceStub.MarketData createMarketData() {
        MarketDataServiceStub.MarketData data = new MarketDataServiceStub.MarketData();        
        data.setCounter(0);
        data.setApplVersionID(1.0);
        data.setMessageType(100);
        data.setSenderCompID(121213.0);
        data.setMsgSeqNum(4);
        data.setSendingTime(00162635);
        data.setTradeDate(20100422);     
        MarketDataServiceStub.MarketDataEntry entries[] = new MarketDataServiceStub.MarketDataEntry[3];
        entries[0] = createMarketEntryData();
        entries[1] = createMarketEntryData();
        entries[2] = createMarketEntryData();
        data.setMdEntries(entries);
        return data;
    }

    private static MarketDataServiceStub.MarketDataEntry createMarketEntryData() {
        MarketDataServiceStub.MarketDataEntry entry = new MarketDataServiceStub.MarketDataEntry();
        entry.setMdUpdateAction(1);
        entry.setMdPriceLevel(2);
        entry.setMdEntryType(7);
        entry.setOpenCloseSettleFlag(3);
        entry.setSecurityID(99);
        entry.setSecurityIDSource(9);
        entry.setRptSeq(2);
        entry.setMdEntryPx(100.0);
        entry.setMdEntryTime(12345);
        entry.setMdEntrySize(50);
        entry.setNumberOfOrders(10);
        entry.setTradingSessionID(2);
        entry.setNetChgPrevDay(10.0);
        entry.setTradeVolume(30);
        entry.setTradeCondition(100.0);//double('W');
        entry.setTickDirection(0);
        entry.setQuoteCondition(67);//double('C');
        entry.setAggressorSide(2);
        entry.setMatchEventIndicator(1);//double('1');
        entry.setDummy1(1);
        entry.setDummy2(2);
        return entry;
    }

    public static MarketDataServiceStub.QuoteRequest createQuoteRequestData() {
        MarketDataServiceStub.QuoteRequest req = new MarketDataServiceStub.QuoteRequest();
        req.setSecurityID(2112);
        req.setApplVersionID(1.0);
        req.setMessageType(100);
        req.setSenderCompID(7881);
        req.setMsgSeqNum(4);
        req.setSendingTime(00162635);
        req.setQuoteReqID(78);//double('R');
        req.setIsEcho(false);
        req.setCounter(0);
        MarketDataServiceStub.RelatedSym related[] = new MarketDataServiceStub.RelatedSym[3];
        related[0] =createRelatedSymData();
        related[1] =createRelatedSymData();
        related[2] =createRelatedSymData();
        req.setRelated(related);
        return req;
    }

    private static MarketDataServiceStub.RelatedSym createRelatedSymData() {
        MarketDataServiceStub.RelatedSym sym = new MarketDataServiceStub.RelatedSym();
        sym.setSymbol(321.0);
        sym.setOrderQuantity(25);
        sym.setSide(1);
        sym.setTransactTime(00162635);
        sym.setQuoteType(1);
        sym.setSecurityID(99);
        sym.setSecurityIDSource(9);
        sym.setDummy1(1);
        sym.setDummy2(2);
        return sym;
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.tcpsocketproject.model;

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
        MarketDataEntry[] mdEntries = new MarketDataEntry[3];        
        mdEntries[0] = createMarketEntryData();
        mdEntries[1] = createMarketEntryData();
        mdEntries[2] = createMarketEntryData();
        data.setMdEntries(mdEntries);
        return data;
    }
    private static MarketDataEntry createMarketEntryData() {
        MarketDataEntry entry = new MarketDataEntry();
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
        RelatedSym[] relatedSym = new RelatedSym[3];        
        relatedSym[0] = createRelatedSymData();
        relatedSym[1] = createRelatedSymData();
        relatedSym[2] = createRelatedSymData();
        req.setRelated(relatedSym);
        return req;
    }

    private static RelatedSym createRelatedSymData() {
        RelatedSym sym = new RelatedSym();
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

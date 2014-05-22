/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.tcpsocketproject;

import dfki.sb.tcpsocketproject.model.MarketData;
import dfki.sb.tcpsocketproject.model.MarketDataEntry;
import dfki.sb.tcpsocketproject.model.QuoteRequest;
import dfki.sb.tcpsocketproject.model.RelatedSym;
import dfki.sb.tcpsocketproject.model.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Shahzad
 */
public class TCPLowlevelServer {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        try {
            ServerSocket clientConnect = new ServerSocket(8081);
            System.out.println("Server started: waiting for clienr on port 8081");
            Socket client = clientConnect.accept(); // blocks
            System.out.println("Client connected ....");
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            dos.flush();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            int type;
            while (true) {
                type = dis.readInt();
                if (type == 2) {
                    handleMarketRequest(dis, dos);
                } else if (type == 1) {
                    handleQuoteRequest(dis, dos);
                } else {
                    break;
                }
            }
            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    private static void handleMarketRequest(DataInputStream dis, DataOutputStream dos) throws IOException, ClassNotFoundException {
        MarketData market = new MarketData();
        market.setIsEcho(dis.readBoolean());
        market.setCounter(dis.readInt());
        market.setSecurityID(dis.readInt());
        market.setApplVersionID(dis.readDouble());
        market.setMessageType(dis.readDouble());
        market.setSenderCompID(dis.readDouble());
        market.setMsgSeqNum(dis.readInt());
        market.setSendingTime(dis.readInt());
        market.setTradeDate(dis.readInt());
        MarketDataEntry[] marketEntry = new MarketDataEntry[dis.readInt()];
        for (int i=0; i < marketEntry.length ; i++) {
            marketEntry[i] = new MarketDataEntry();
            marketEntry[i].setMdUpdateAction(dis.readInt());
            marketEntry[i].setMdPriceLevel(dis.readInt());
            marketEntry[i].setMdEntryType(dis.readDouble());
            marketEntry[i].setOpenCloseSettleFlag(dis.readInt());
            marketEntry[i].setSecurityIDSource(dis.readInt());
            marketEntry[i].setSecurityID(dis.readInt());
            marketEntry[i].setRptSeq(dis.readInt());
            marketEntry[i].setMdEntryPx(dis.readDouble());
            marketEntry[i].setMdEntryTime(dis.readInt());
            marketEntry[i].setMdEntrySize(dis.readInt());
            marketEntry[i].setNumberOfOrders(dis.readInt());
            marketEntry[i].setTradingSessionID(dis.readDouble());
            marketEntry[i].setNetChgPrevDay(dis.readDouble());
            marketEntry[i].setTradeVolume(dis.readInt());
            marketEntry[i].setTradeCondition(dis.readDouble());
            marketEntry[i].setTickDirection(dis.readDouble());
            marketEntry[i].setQuoteCondition(dis.readDouble());
            marketEntry[i].setAggressorSide(dis.readInt());
            marketEntry[i].setMatchEventIndicator(dis.readDouble());
            marketEntry[i].setDummy1(dis.readDouble());
            marketEntry[i].setDummy2(dis.readInt());
        }
        market.setMdEntries(marketEntry);
        writeMarketDataToOutputStream(dos, market); 
    }

    public static void handleQuoteRequest(DataInputStream dis, DataOutputStream dos) throws IOException, ClassNotFoundException {
        QuoteRequest quote = new QuoteRequest();
        quote.setIsEcho(dis.readBoolean());
        quote.setCounter(dis.readInt());
        quote.setSecurityID(dis.readInt());
        quote.setApplVersionID(dis.readDouble());
        quote.setMessageType(dis.readDouble());
        quote.setSenderCompID(dis.readDouble());
        quote.setMsgSeqNum(dis.readInt());
        quote.setSendingTime(dis.readInt());
        quote.setQuoteReqID(dis.readDouble());
        quote.setIsEcho(false);
        RelatedSym[] sym = new RelatedSym[dis.readInt()];
        for (int i = 0; i < sym.length ; i++) {
            sym[i] = new RelatedSym();
            sym[i].setSymbol(dis.readDouble());
            sym[i].setOrderQuantity(dis.readLong());
            sym[i].setSide(dis.readInt());
            sym[i].setTransactTime(dis.readLong());
            sym[i].setQuoteType(dis.readInt());
            sym[i].setSecurityID(dis.readInt());
            sym[i].setSecurityIDSource(dis.readInt());
            sym[i].setDummy1(dis.readDouble());
            sym[i].setDummy2(dis.readInt());
        }
        quote.setRelated(sym);
        writeQuoteDataToOutputStream(dos, quote);  
    }
    
    private static void writeQuoteDataToOutputStream(DataOutputStream dos, QuoteRequest quote) throws IOException {
        dos.writeBoolean(quote.isIsEcho());
        dos.writeInt(quote.getCounter());
        dos.writeInt(quote.getSecurityID());
        dos.writeDouble(quote.getApplVersionID());
        dos.writeDouble(quote.getMessageType());
        dos.writeDouble(quote.getSenderCompID());
        dos.writeInt(quote.getMsgSeqNum());
        dos.writeInt(quote.getSendingTime());
        dos.writeDouble(quote.getQuoteReqID());
        dos.writeInt(quote.getRelated().length);
        for (RelatedSym related : quote.getRelated()) {
            dos.writeDouble(related.getSymbol());
            dos.writeLong(related.getOrderQuantity());
            dos.writeInt(related.getSide());
            dos.writeLong(related.getTransactTime());
            dos.writeInt(related.getQuoteType());
            dos.writeInt(related.getSecurityID());
            dos.writeInt(related.getSecurityIDSource());
            dos.writeDouble(related.getDummy1());
            dos.writeInt(related.getDummy2());
        }
        dos.flush();
    }

    private static void writeMarketDataToOutputStream(DataOutputStream dos, MarketData market) throws IOException {
        dos.writeBoolean(market.isIsEcho());
        dos.writeInt(market.getCounter());
        dos.writeInt(market.getSecurityID());
        dos.writeDouble(market.getApplVersionID());
        dos.writeDouble(market.getMessageType());
        dos.writeDouble(market.getSenderCompID());
        dos.writeInt(market.getMsgSeqNum());
        dos.writeInt(market.getSendingTime());
        dos.writeInt(market.getTradeDate());
        dos.writeInt(market.getMdEntries().length);
        for (MarketDataEntry marketEntry : market.getMdEntries()) {
            dos.writeInt(marketEntry.getMdUpdateAction());
            dos.writeInt(marketEntry.getMdPriceLevel());
            dos.writeDouble(marketEntry.getMdEntryType());
            dos.writeInt(marketEntry.getOpenCloseSettleFlag());
            dos.writeInt(marketEntry.getSecurityIDSource());
            dos.writeInt(marketEntry.getSecurityID());
            dos.writeInt(marketEntry.getRptSeq());
            dos.writeDouble(marketEntry.getMdEntryPx());
            dos.writeInt(marketEntry.getMdEntryTime());
            dos.writeInt(marketEntry.getMdEntrySize());
            dos.writeInt(marketEntry.getNumberOfOrders());
            dos.writeDouble(marketEntry.getTradingSessionID());
            dos.writeDouble(marketEntry.getNetChgPrevDay());
            dos.writeInt(marketEntry.getTradeVolume());
            dos.writeDouble(marketEntry.getTradeCondition());
            dos.writeDouble(marketEntry.getTickDirection());
            dos.writeDouble(marketEntry.getQuoteCondition());
            dos.writeInt(marketEntry.getAggressorSide());
            dos.writeDouble(marketEntry.getMatchEventIndicator());
            dos.writeDouble(marketEntry.getDummy1());
            dos.writeInt(marketEntry.getDummy2());
        }
        dos.flush();
    }
}

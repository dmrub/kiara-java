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
import java.net.Socket;

/**
 *
 * @author Shahzad
 */
public class TCPClient {

    public static void main(String args[]) {
        try {
            Socket sender = new Socket("localhost", 8081);
            if (sender.isConnected()) {
                int numMessages = 10000;                
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(sender.getOutputStream()));
                dos.flush();                
                DataInputStream dis = new DataInputStream(new BufferedInputStream(sender.getInputStream()));
                preprationMethod(dos, dis);        
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < numMessages; i++) {
                    // Send 10 MarketDatas for each QuoteRequest
                    if (i % 10 == 5 ) {                        
                        handleQuoteData(dos, dis);
                    } else {
                        handleMarketData(dos, dis);
                    }
                }                                    
                long finishTime = System.currentTimeMillis();
                dos.writeInt(-1);
                dos.flush();
                dos.close();
                dis.close();
                long difference = finishTime - startTime;
                difference = difference * 1000;
                double latency = (double) difference / (numMessages * 2.0);
                System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n\n");
                System.out.println("Finished");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void preprationMethod(DataOutputStream dos, DataInputStream dis) throws IOException {
        for (int i = 0; i < 20000; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5 ) {
                handleQuoteData(dos, dis);
            } else {
                handleMarketData(dos, dis);
            }
        }
    }
    
        

    private static void handleQuoteData(DataOutputStream dos, DataInputStream dis) throws IOException {
        dos.writeInt(1);
        QuoteRequest quote = Util.createQuoteRequestData();
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
        readQuoteRequestObject(dis);
    }

    private static void readQuoteRequestObject(DataInputStream dis) throws IOException {
        dis.readBoolean();        
        dis.readInt();
        dis.readInt();
        dis.readDouble();
        dis.readDouble();
        dis.readDouble();
        dis.readInt();
        dis.readInt();
        dis.readDouble();
        int loopEnd = dis.readInt();
        for (int i=0 ; i < loopEnd ;i++) {
            dis.readDouble();
            dis.readLong();
            dis.readInt();
            dis.readLong();
            dis.readInt();
            dis.readInt();
            dis.readInt();
            dis.readDouble();
            dis.readInt();
        }
    }    

    private static void handleMarketData(DataOutputStream dos, DataInputStream dis) throws IOException {
        dos.writeInt(2);
        MarketData market = Util.createMarketData();
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
        readMarketDataObject(dis);
    }

    private static void readMarketDataObject(DataInputStream dis) throws IOException {
        dis.readBoolean();
        dis.readInt();
        dis.readInt();
        dis.readDouble();
        dis.readDouble();
        dis.readDouble();
        dis.readInt();
        dis.readInt();
        dis.readInt();
        int loopEnd = dis.readInt();
        for (int i=0 ;i < loopEnd ; i++) {
            dis.readInt();
            dis.readInt();
            dis.readDouble();
            dis.readInt();
            dis.readInt();
            dis.readInt();
            dis.readInt();
            dis.readDouble();
            dis.readInt();
            dis.readInt();
            dis.readInt();
            dis.readDouble();
            dis.readDouble();
            dis.readInt();
            dis.readDouble();
            dis.readDouble();
            dis.readDouble();
            dis.readInt();
            dis.readDouble();
            dis.readDouble();
            dis.readInt();
        }    
    }
}

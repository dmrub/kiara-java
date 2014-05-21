/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.tcpsocketproject;

import dfki.sb.tcpsocketproject.model.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                int numMessages = 1000;                
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(sender.getOutputStream()));
                oos.flush();                
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(sender.getInputStream()));
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < numMessages; i++) {
                    // Send 10 MarketDatas for each QuoteRequest
                    if (i % 10 == 5) {
                        oos.writeInt(1);
                        oos.writeObject(Util.createQuoteRequestData());
                        oos.flush();                        
                        ois.readObject();   
                    } else {                        
                        oos.writeInt(2);
                        oos.writeObject(Util.createMarketData());
                        oos.flush();                        
                        ois.readObject();                        
                    }
                }
                oos.writeInt(-1);
                oos.flush();                        
                long finishTime = System.currentTimeMillis();
                long difference = finishTime - startTime;
                difference = difference * 1000;
                double latency = (double) difference / (numMessages * 2.0);
                System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n\n");
                System.out.println("Finished");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    
        

/*    private static void handleQuoteData(DataOutputStream dos, DataInputStream dis) throws IOException {
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
        readRelatedSystemObject(dis, quote);
    }

    private static void readRelatedSystemObject(DataInputStream dis, QuoteRequest quote) throws IOException {
        dis.readBoolean();        
        dis.readInt();
        dis.readInt();
        dis.readDouble();
        dis.readDouble();
        dis.readDouble();
        dis.readInt();
        dis.readInt();
        dis.readDouble();
        for (RelatedSym related : quote.getRelated()) {
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
    */
}

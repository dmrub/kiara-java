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
package dfki.sb.rabbitmqjava;

/**
 *
 * @author Administrator
 */
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import dfki.sb.rabbitmqjava.model.MarketData;
import dfki.sb.rabbitmqjava.model.MarketDataEntry;
import dfki.sb.rabbitmqjava.model.QuoteRequest;
import dfki.sb.rabbitmqjava.model.RelatedSym;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RabbitMQServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";
    
    private static byte[] handleMarketRequest(DataInputStream dis) throws IOException, ClassNotFoundException {
        MarketData market = new MarketData();
        market.setIsEcho(dis.readBoolean());
        market.setIsEcho(true);
        market.setCounter(dis.readInt());
        market.setSecurityID(dis.readInt());
        market.setApplVersionID(dis.readDouble());
        market.setMessageType(dis.readDouble());
        market.setSenderCompID(dis.readDouble());
        market.setMsgSeqNum(dis.readInt());
        market.setSendingTime(dis.readInt());
        market.setTradeDate(dis.readInt());
        MarketDataEntry[] marketDataEntry = new MarketDataEntry[dis.readInt()];
        for (int i = 0; i < marketDataEntry.length; i++) {
            marketDataEntry[i] = new MarketDataEntry();
            marketDataEntry[i].setMdUpdateAction(dis.readInt());
            marketDataEntry[i].setMdPriceLevel(dis.readInt());
            marketDataEntry[i].setMdEntryType(dis.readDouble());
            marketDataEntry[i].setOpenCloseSettleFlag(dis.readInt());
            marketDataEntry[i].setSecurityIDSource(dis.readInt());
            marketDataEntry[i].setSecurityID(dis.readInt());
            marketDataEntry[i].setRptSeq(dis.readInt());
            marketDataEntry[i].setMdEntryPx(dis.readDouble());
            marketDataEntry[i].setMdEntryTime(dis.readInt());
            marketDataEntry[i].setMdEntrySize(dis.readInt());
            marketDataEntry[i].setNumberOfOrders(dis.readInt());
            marketDataEntry[i].setTradingSessionID(dis.readDouble());
            marketDataEntry[i].setNetChgPrevDay(dis.readDouble());
            marketDataEntry[i].setTradeVolume(dis.readInt());
            marketDataEntry[i].setTradeCondition(dis.readDouble());
            marketDataEntry[i].setTickDirection(dis.readDouble());
            marketDataEntry[i].setQuoteCondition(dis.readDouble());
            marketDataEntry[i].setAggressorSide(dis.readInt());
            marketDataEntry[i].setMatchEventIndicator(dis.readDouble());
            marketDataEntry[i].setDummy1(dis.readDouble());
            marketDataEntry[i].setDummy2(dis.readInt());
        }
        market.setMdEntries(marketDataEntry);

        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(byteOut))) {
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
            return byteOut.toByteArray();
        }
    }

    public static byte[] handleQuoteRequest(DataInputStream dis) throws IOException, ClassNotFoundException {
        QuoteRequest quote = new QuoteRequest();
        quote.setIsEcho(dis.readBoolean());
        quote.setIsEcho(true);
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
        for (int i = 0; i < sym.length; i++) {
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
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(byteOut))) {
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
            return byteOut.toByteArray();
        }
    }

    public static void main(String[] argv) {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            System.out.println("Starting server waiting for client requests:");
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties.Builder()
                        .correlationId(props.getCorrelationId())
                        .build();
                DataInputStream dis = new DataInputStream(new BufferedInputStream(
                        new ByteArrayInputStream(delivery.getBody())));
                try {
                    int type = dis.readInt();
                    byte[] response;
                    if (type == 2) {
                        response = handleMarketRequest(dis);
                    } else {
                        response = handleQuoteRequest(dis);
                    }
                    channel.basicPublish("", props.getReplyTo(), replyProps, response);
                    dis.close();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        } catch (IOException | InterruptedException | ShutdownSignalException | ConsumerCancelledException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}

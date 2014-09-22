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
import com.rabbitmq.client.QueueingConsumer;
import dfki.sb.rabbitmqjava.model.MarketData;
import dfki.sb.rabbitmqjava.model.MarketDataEntry;
import dfki.sb.rabbitmqjava.model.QuoteRequest;
import dfki.sb.rabbitmqjava.model.RelatedSym;
import dfki.sb.rabbitmqjava.model.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class RabbitMQClient {

    private void sendQuoteRequest(QuoteRequest quote) throws IOException, InterruptedException {        
        String corrId = UUID.randomUUID().toString();
        BasicProperties props = new BasicProperties.Builder().correlationId(corrId).
                replyTo(replyQueueName).build();
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); 
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(byteOut))) {
            dos.writeInt(1);
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
            channel.basicPublish("", requestQueueName, props, byteOut.toByteArray());
        }
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                readQuoteRequest(delivery.getBody());
                break;
            }
        }
    }
    
    private void readQuoteRequest(byte[] body) throws IOException{
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
                new ByteArrayInputStream(body)))) {
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
    }

    private void readMarketData(byte[] body) throws IOException{
        DataInputStream dis = new DataInputStream(new BufferedInputStream(
                            new ByteArrayInputStream(body)));
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
    
    private void sendMarketData(MarketData market) throws IOException, InterruptedException {
        String corrId = UUID.randomUUID().toString();
        BasicProperties props = new BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); 
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(byteOut))) {
            dos.writeInt(2);
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
            channel.basicPublish("", requestQueueName, props, byteOut.toByteArray());
        }
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                readMarketData(delivery.getBody());
                break;
            }
        } 
    }

    private final Connection connection;
    private final Channel channel;
    private final String requestQueueName = "rpc_queue";
    private final String replyQueueName;
    private final QueueingConsumer consumer;

    public RabbitMQClient() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] argv) {
        int numMessages = 10000;
        RabbitMQClient rpcClient = null;        
        try {
            rpcClient = new RabbitMQClient();
            sendMessages(20000,rpcClient);
            long startTime = System.currentTimeMillis();
            sendMessages(numMessages, rpcClient);
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            double latency = (double) difference / (numMessages * 2.0);
            System.out.println(String.format("\n\nAverage latency in microseconds %.3f\n\n\n", latency));
            System.out.println("Finished");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rpcClient != null) {
                try {
                    rpcClient.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private static void sendMessages(int numberOfMessages,RabbitMQClient rpcClient) throws InterruptedException, IOException {
        for (int i = 0; i < numberOfMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                rpcClient.sendQuoteRequest(Util.createQuoteRequestData());
            } else {
                rpcClient.sendMarketData(Util.createMarketData());
            }
        }
    }
}

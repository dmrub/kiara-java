/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import dfki.sb.rabbitmqjava.model.QuoteRequest;
import dfki.sb.rabbitmqjava.model.Util;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.lang.SerializationUtils;

public class RabbitMQObjectStreamClient {

    private QuoteRequest sendQuoteRequest(QuoteRequest quoteObject) throws IOException, InterruptedException {
        QuoteRequest response = null;
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, SerializationUtils.serialize(quoteObject));
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = (QuoteRequest) SerializationUtils.deserialize(delivery.getBody());
                break;
            }
        }
        return response;
    }
    
    private void sendDesiconnectSignal() throws IOException, InterruptedException {
        String corrId = UUID.randomUUID().toString();
        BasicProperties props = new BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, null);
    }

    private MarketData sendMarketData(MarketData marketObject) throws IOException, InterruptedException {
        MarketData response = null;
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, SerializationUtils.serialize(marketObject));
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = (MarketData) SerializationUtils.deserialize(delivery.getBody());
                break;
            }
        }
        return response;
    }

    private final Connection connection;
    private final Channel channel;
    private final String requestQueueName = "rpc_queue";
    private final String replyQueueName;
    private final QueueingConsumer consumer;

    public RabbitMQObjectStreamClient() throws Exception {
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
        RabbitMQObjectStreamClient rpcClient = null;
        String response = null;
        try {
            rpcClient = new RabbitMQObjectStreamClient();
            sendMessages(20000,rpcClient);
            long startTime = System.currentTimeMillis();
            sendMessages(numMessages, rpcClient);
            long finishTime = System.currentTimeMillis();
            long difference = finishTime - startTime;
            difference = difference * 1000;
            double latency = (double) difference / (numMessages * 2.0);
            rpcClient.sendDesiconnectSignal();
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

    private static void sendMessages(int numberOfMessages,RabbitMQObjectStreamClient rpcClient) throws InterruptedException, IOException {
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

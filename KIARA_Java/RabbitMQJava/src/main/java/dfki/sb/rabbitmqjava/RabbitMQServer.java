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
import org.apache.commons.lang.SerializationUtils;

public class RabbitMQServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";
    private static int messageCounter = 0;

    public static MarketData sendMarketData(MarketData marketData) {
        int counter = messageCounter;
        ++messageCounter;
        MarketData returnData = marketData;
        returnData.setIsEcho(true);
        return returnData;
    }

    public static QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) {
        int counter = messageCounter;
        ++messageCounter;

        QuoteRequest returnRequest = quoteRequest;
        returnRequest.setIsEcho(true);
        return returnRequest;
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
            System.out.println("Server started waiting for client requests:");            
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties.Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
                    Object obj = SerializationUtils.deserialize(delivery.getBody());
                    if (obj instanceof MarketData) {
                        MarketData response = sendMarketData((MarketData) obj);
                        channel.basicPublish("", props.getReplyTo(), replyProps, SerializationUtils.serialize(response));
                    } else {
                        QuoteRequest response = sendQuoteRequest((QuoteRequest) obj);
                        channel.basicPublish("", props.getReplyTo(), replyProps, SerializationUtils.serialize(response));
                    }
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}

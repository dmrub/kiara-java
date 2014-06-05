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
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import dfki.sb.rabbitmqjava.model.MarketData;
import dfki.sb.rabbitmqjava.model.QuoteRequest;
import java.io.IOException;
import org.apache.commons.lang.SerializationUtils;

public class RabbitMQObjectStreamServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";
    private static int messageCounter = 0;
    private static Connection connection = null;
    public static MarketData sendMarketData(MarketData marketData) {
        ++messageCounter;
        MarketData returnData = marketData;
        returnData.setIsEcho(true);
        return returnData;
    }

    public static QuoteRequest sendQuoteRequest(QuoteRequest quoteRequest) {
        ++messageCounter;
        QuoteRequest returnRequest = quoteRequest;
        returnRequest.setIsEcho(true);
        return returnRequest;
    }

    public static void main(String[] argv) {
        
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
                    byte[] body = delivery.getBody();
                    if(body.length==0){                        
                        break;
                    }else{
                        Object obj = SerializationUtils.deserialize(body);
                        if (obj instanceof MarketData) {
                            MarketData response = sendMarketData((MarketData) obj);
                            channel.basicPublish("", props.getReplyTo(), replyProps, SerializationUtils.serialize(response));
                        } else {
                            QuoteRequest response = sendQuoteRequest((QuoteRequest) obj);
                            channel.basicPublish("", props.getReplyTo(), replyProps, SerializationUtils.serialize(response));
                        }
                    }
                } catch (IOException e) {
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

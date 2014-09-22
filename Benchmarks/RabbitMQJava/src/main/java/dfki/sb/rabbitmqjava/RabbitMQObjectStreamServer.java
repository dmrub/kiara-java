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
            System.out.println("Starting server waiting for client requests:");            
            processSendAndRecivePackets(consumer, channel);
            if(argv!=null && argv.length > 0 && argv[0].equalsIgnoreCase("infinite")){
                while(true){
                    System.out.println("Waiting for next client");  
                    processSendAndRecivePackets(consumer, channel);
                }
            }
        } catch (IOException | InterruptedException e) {
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

    private static void processSendAndRecivePackets(QueueingConsumer consumer, Channel channel) throws InterruptedException, IOException {
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
    }
}

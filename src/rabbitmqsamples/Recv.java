package rabbitmqsamples;

import java.io.IOException;

import com.rabbitmq.client.BlockedListener;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setVirtualHost("samples");
		Connection connection = factory.newConnection();
		
		connection.addBlockedListener(new BlockedListener() {
			
			@Override
			public void handleUnblocked() throws IOException {
				System.out.println("handleUnblocked....");
			}
			
			@Override
			public void handleBlocked(String reason) throws IOException {
				System.out.println("handleBlocked ... ");
				
			}
		});
		
		connection.addShutdownListener(new ShutdownListener() {
			
			@Override
			public void shutdownCompleted(ShutdownSignalException cause) {
				System.out.println("Reciever connection closed - "+ cause);
			}
		});
		
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}
	}
}
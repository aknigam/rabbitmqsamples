package rabbitmqsamples;

import com.rabbitmq.client.AMQP.Confirm.SelectOk;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class Send {
	
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
      	      
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("samples");
    
    Connection connection = factory.newConnection();
    connection.addShutdownListener(new ShutdownListener() {
		
		@Override
		public void shutdownCompleted(ShutdownSignalException cause) {
			System.out.println("Connection closed : "+ cause.toString());
		}
	});
    Channel channel = connection.createChannel();
//    SelectOk s = channel.confirmSelect();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello World! Anand";
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    
//    System.out.println(channel.getNextPublishSeqNo());
    
    System.out.println(" [x] Sent '" + message + "'");
    channel.close();
    connection.close();
  }
}
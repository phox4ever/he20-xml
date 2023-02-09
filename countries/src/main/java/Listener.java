import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;


// add folowing jars to your classpath
// you can find them in your activemq installation under
// ....\apache-activemq-5.16.3\lib
//	1. activemq-all-5.16.3.jar
//	2. activemq-client-5.16.3.jar

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
 
public class Listener {
 
    // URL of the JMS server. Change URL accoring your Ubuntu
    private static String url = "tcp://localhost:6161";
 
    // Name of the queue we will receive messages from
    private static String subject = "JCG_QUEUE";
 
    public static void main(String[] args) throws JMSException {
        // Getting JMS connection from the server
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
 
        // Creating session for seding messages
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
 
        // Getting the queue 'JCG_QUEUE'
        Destination destination = session.createQueue(subject);
 
    	// MessageConsumer is used for receiving (consuming) messages
    	MessageConsumer consumer = session.createConsumer(destination);

    	int i=0;
    	while (true) { 
        	// Here we receive the message.
        	Message message = consumer.receive();
 
        	// We will be using TestMessage in our example. MessageProducer sent us a TextMessage
        	// so we must cast to it to get access to its .getText() method.
        	if (message instanceof TextMessage) {
        		TextMessage textMessage = (TextMessage) message;
        		//System.out.println("Message no "+i+" Received message '" + textMessage.getText() + "'");
        		i++;
                //Parse XML Document from message
                Parser.parseXML(textMessage.getText());
        	}
        }
    	//connection.close();
    }
}

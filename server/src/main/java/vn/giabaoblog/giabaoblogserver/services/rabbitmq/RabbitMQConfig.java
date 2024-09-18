package vn.giabaoblog.giabaoblogserver.services.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_QUEUE = "emailQueue";
    public static final String EMAIL_EXCHANGE = "emailExchange";
    public static final String EMAIL_ROUTING_KEY = "emailRoutingKey";

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    @Bean
    public Connection connection(ConnectionFactory connectionFactory) throws Exception {
        return connectionFactory.newConnection();
    }

    @Bean
    public Channel channel(Connection connection) throws Exception {
        Channel channel = connection.createChannel();
        // Tạo queue
        channel.queueDeclare(EMAIL_QUEUE, true, false, false, null);
        // Tạo exchange
        channel.exchangeDeclare(EMAIL_EXCHANGE, "topic");
        // Tạo binding giữa queue và exchange với routing key
        channel.queueBind(EMAIL_QUEUE, EMAIL_EXCHANGE, EMAIL_ROUTING_KEY);
        return channel;
    }
}

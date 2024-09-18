package vn.giabaoblog.giabaoblogserver.services.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import vn.giabaoblog.giabaoblogserver.data.dto.request.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducer {

    @Autowired
    private ConnectionFactory connectionFactory;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void sendEmailMessage(EmailMessage emailMessage) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(RabbitMQConfig.EMAIL_EXCHANGE, "topic", true);
            String message = objectMapper.writeValueAsString(emailMessage);

            channel.basicPublish(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_ROUTING_KEY, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

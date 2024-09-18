package vn.giabaoblog.giabaoblogserver.services.rabbitmq;

import com.rabbitmq.client.*;
import vn.giabaoblog.giabaoblogserver.data.dto.request.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.services.EmailService;

import java.io.IOException;

@Service
public class RabbitConsumer {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private EmailService emailService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void consumeMessages() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(RabbitMQConfig.EMAIL_QUEUE, true, false, false, null);
            channel.exchangeDeclare(RabbitMQConfig.EMAIL_EXCHANGE, "topic", true);
            channel.queueBind(RabbitMQConfig.EMAIL_QUEUE, RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_ROUTING_KEY);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String messageBody = new String(delivery.getBody(), "UTF-8");
                EmailMessage emailMessage = objectMapper.readValue(messageBody, EmailMessage.class);

                System.out.println(" [x] Received '" + emailMessage + "'");

                // Xử lý email
                if (emailMessage.getType().equals("registration")) {
                    emailService.sendRegistrationConfirmationEmail(emailMessage.getTo(), emailMessage.getContent());
                } else if (emailMessage.getType().equals("reset")) {
                    emailService.sendResetPasswordEmail(emailMessage.getTo(), emailMessage.getContent());
                }
            };

            channel.basicConsume(RabbitMQConfig.EMAIL_QUEUE, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

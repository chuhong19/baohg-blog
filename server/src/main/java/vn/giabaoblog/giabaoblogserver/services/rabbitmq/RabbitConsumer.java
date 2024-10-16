package vn.giabaoblog.giabaoblogserver.services.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.services.EmailService;

@Service
public class RabbitConsumer {

    private static final String QUEUE_NAME = "myQueue";

    @Autowired
    private Connection rabbitConnection;

    @Autowired
    private EmailService emailService;

    public void consumeMessage() throws Exception {
        try (Channel channel = rabbitConnection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                String[] parts = message.split(",");
                String email = parts[0];
                String confirmationLink = parts[1];

                emailService.sendRegistrationConfirmationEmail(email, confirmationLink);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        }
    }
}

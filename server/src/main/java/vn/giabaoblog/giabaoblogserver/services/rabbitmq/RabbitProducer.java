package vn.giabaoblog.giabaoblogserver.services.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.data.dto.request.EmailMessage;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitProducer {

    private static final String QUEUE_NAME = "myQueue";

    @Autowired
    private Connection rabbitConnection;

    public void sendEmailMessage(EmailMessage message) {
        try (Channel channel = rabbitConnection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getContent().getBytes("UTF-8"));
            System.out.println("Sent message: " + message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }


}

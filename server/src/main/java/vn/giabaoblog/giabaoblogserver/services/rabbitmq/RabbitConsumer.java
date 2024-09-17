package vn.giabaoblog.giabaoblogserver.services.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.data.dto.request.EmailMessage;
import vn.giabaoblog.giabaoblogserver.services.EmailService;

@Slf4j
@Service
public class RabbitConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveMessage(EmailMessage emailMessage) {
        log.info("Email message: " + emailMessage);
        if (emailMessage.getType().equals("registration")) {
            emailService.sendRegistrationConfirmationEmail(emailMessage.getTo(), emailMessage.getContent());
        } else if (emailMessage.getType().equals("reset")) {
            emailService.sendResetPasswordEmail(emailMessage.getTo(), emailMessage.getContent());
        }
    }
}

package vn.giabaoblog.giabaoblogserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendResetPasswordEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Play from Java-App");
        message.setText("This is your reset token:\n" + resetToken);

        javaMailSender.send(message);
    }

    public void sendRegistrationConfirmationEmail(String to, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Java-App!");
        message.setText("Thank you for registering with our application. "
                + "Please confirm your registration by clicking the following link:\n" + confirmationLink);

        javaMailSender.send(message);
    }

}

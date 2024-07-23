package vn.giabaoblog.giabaoblogserver.services.support;

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
}

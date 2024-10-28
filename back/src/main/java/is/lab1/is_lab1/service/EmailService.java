package is.lab1.is_lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.model.IsUser;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.url}")
    private String url;

    public void sendPasswordResetEmail(IsUser user, String token) {
        String resetUrl =  url + "/auth/password-reset?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Сброс пароля");
        message.setText("Здравствуйте!\n\n" +
                "Для сброса пароля перейдите по ссылке ниже:\n" +
                resetUrl + "\n\n" +
                "Если вы не запрашивали сброс пароля, проигнорируйте это письмо.\n\n");

        mailSender.send(message);
    }
}
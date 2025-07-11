package ch.bbw.pr.tresorbackend.service;

import ch.bbw.pr.tresorbackend.model.MailConfig;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailSender {

    public static void sendMail(MailConfig config, String subject, String text) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(config.tls));
        props.put("mail.smtp.host", config.smtpHost);
        props.put("mail.smtp.port", String.valueOf(config.smtpPort));

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.username, config.password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.username));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(config.recipient)
        );
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);

        System.out.println("Email sent successfully to " + config.recipient);
    }


}


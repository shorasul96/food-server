package com.shorasul96.jwtapp.sercurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@PropertySource("classpath:application-dev.properties")
public class GoogleVerification {

    @Value("${google.gmail.username}")
    private static String username;
    @Value("${google.gmail.password}")
    private static String password;

    private static String validationCode() {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[5];
        for (int i = 0; i < 5; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }

    public static void sendVerification(String recepient) throws MessagingException {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");


        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = prepareMessage(session, username, recepient);
        assert message != null;
        Transport.send(message);
        System.out.println("SUCCESS");
    }

    private static Message prepareMessage(Session session, String username, String recipient) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Verify your linked email");
            message.setText("Verification code:" + validationCode());
            message.setSentDate(new Date());
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(GoogleVerification.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;

    }

}


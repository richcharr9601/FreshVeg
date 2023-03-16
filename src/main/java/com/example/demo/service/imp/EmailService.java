package com.example.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    public EmailService() {
    }

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String email, String username) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("your_email@gmail.com");
        helper.setTo(email);
        helper.setSubject("Registration success");
        helper.setText("Dear " + username + ",\n\nThank you for registering with our website.");

        javaMailSender.send(message);
    }
    
}

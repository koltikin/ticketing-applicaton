package com.cydeo.service.impl;

import com.cydeo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final Environment environment;
    private final JavaMailSender mailSender;
    @Override
    public void sendEmail(String sendTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(environment.getProperty("EMAIL"));
        mailMessage.setTo(sendTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}

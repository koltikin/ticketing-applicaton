package com.cydeo.service.impl;

import com.cydeo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    public static final String UTF_8ENCODING = "UTF-8";
    private final Environment environment;
    private final JavaMailSender mailSender;
    private final SimpleMailMessage mailMessage;
    private final MimeMessage mimeMessage;
    @Override
    @Async
    public void sendSimpleMessage(String userEmail, String subject, String message) {

//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom(environment.getProperty("EMAIL"));
//        mailMessage.setFrom(System.getProperty("EMAIL"));

                mailMessage.setTo(userEmail);
                mailMessage.setSubject(subject);
                mailMessage.setText(message);
                mailSender.send(mailMessage);


    }

    @SneakyThrows
    @Override
    @Async
    public void sendMessageWithAttachment(String sendTo, String subject, String message) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, UTF_8ENCODING);

        helper.setTo(sendTo);
        helper.setSubject(subject);
        helper.setText(message);

//        Add attachment
        FileSystemResource image = new FileSystemResource("src/main/resources/static/images/cydeo-logo.svg");
        FileSystemResource image1 = new FileSystemResource("src/main/resources/static/images/CydeoLogo_01.png");
        FileSystemResource image2 = new FileSystemResource("src/main/resources/templates/email/PasswordResetEmail.html");
        helper.addAttachment(image.getFilename(),image);
        helper.addAttachment(image1.getFilename(),image1);
        helper.addAttachment(image2.getFilename(),image2);

        mailSender.send(mimeMessage);

        mailSender.send(mimeMessage);

    }

    @Override
    public void sendHtmlMessageWithImage(String sendTo, String subject, String message) {

    }
}



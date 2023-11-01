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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    public static final String UTF_8ENCODING = "UTF-8";
    private final Environment environment;
    private final JavaMailSender mailSender;
    private final SimpleMailMessage mailMessage;
    private final TemplateEngine templateEngine;
    private final MimeMessageHelper helper;
    private final Context context;
    private final BodyPart bodyPart;
    private final Multipart multipart;
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
        FileSystemResource image2 = new FileSystemResource("src/main/resources/templates/email/emailTemplate.html");
        helper.addAttachment(image.getFilename(),image);
        helper.addAttachment(image1.getFilename(),image1);
        helper.addAttachment(image2.getFilename(),image2);

        mailSender.send(mimeMessage);

        mailSender.send(mimeMessage);

    }

    @SneakyThrows
    @Async
    @Override
    public void sendHtmlMessageWithImage(String sendTo, String subject, String title, String body, String url ) {

//        context.setVariable("messageTitle", title);
//        context.setVariable("messageBody", body);
//        context.setVariable("url", url);
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

        helper.setPriority(1);
        helper.setTo(sendTo);
        helper.setSubject(subject);

        /** before add message to mimeMessage add logo to the html template */

//        Context context = new Context();
        context.setVariables(Map.of("messageTitle",title,"messageBody",body,"url", url));

        String message = templateEngine.process("/email/emailTemplate",context);

        BodyPart bodyPartContent = new MimeBodyPart();
        bodyPartContent.setContent(message,"text/html");

//        Multipart multipart = new MimeMultipart("related");
        multipart.addBodyPart(bodyPartContent);

        BodyPart bodyPartImg = new MimeBodyPart();
        DataSource dataSource = new FileDataSource("src/main/resources/static/images/cydeo-logo.png");
        bodyPartImg.setDataHandler(new DataHandler(dataSource));
        bodyPartImg.setHeader("Content-Id","logo");

        multipart.addBodyPart(bodyPartImg);

        mimeMessage.setContent(multipart);

        mailSender.send(mimeMessage);

    }
}



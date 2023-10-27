package com.cydeo.service.impl;

import com.cydeo.Repository.AccountConfirmationRepository;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.AccountConfirmation;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
//import com.cydeo.service.ConfirmationService;
import com.cydeo.service.EmailService;
import com.cydeo.service.UserService;
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
    private final AccountConfirmationRepository confirmationRepository;
    private final SimpleMailMessage mailMessage;
    @Override
    public void sendUserVerificationEmail(String userEmail, String subject, String message) {

//                SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom(environment.getProperty("EMAIL"));
//        mailMessage.setFrom(System.getProperty("EMAIL"));

                mailMessage.setTo(userEmail);
                mailMessage.setSubject(subject);
                mailMessage.setText(message);

                mailSender.send(mailMessage);
    }
}


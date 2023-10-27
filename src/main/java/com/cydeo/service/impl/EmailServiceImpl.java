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
    private final UserService userService;
    private final UserMapper userMapper;
    private final AccountConfirmationRepository confirmationRepository;
    @Override
    public void sendEmail(String userEmail) {
        if (userService.isUserExistByEmail(userEmail)) {
            User user = userMapper.convertToEntity(userService.findById(userEmail));
            if (!user.isEnabled()){
                SimpleMailMessage mailMessage = new SimpleMailMessage();

//        mailMessage.setFrom(environment.getProperty("EMAIL"));
//        mailMessage.setFrom(System.getProperty("EMAIL"));

                String token = confirmationRepository.findTokenByUserName(user.getUserName());
                String message = "Click the link blow to Verify Your Cydeo Ticketing Account.\n\n " +
                        "http://localhost:8080/user/verify?token=" + token + "\n\n" +
                        "from: cydeo.ticketing@gmail.com";

                mailMessage.setTo(userEmail);
                mailMessage.setSubject("Verify Cydeo Ticketing Account");
                mailMessage.setText(message);

                mailSender.send(mailMessage);

            }
        }

    }
}

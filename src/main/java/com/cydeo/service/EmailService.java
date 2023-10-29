package com.cydeo.service;

import org.springframework.scheduling.annotation.Async;


public interface EmailService {

    void sendSimpleMessage(String sendTo, String subject, String message);

    void sendMessageWithAttachment(String sendTo, String subject, String message);
}

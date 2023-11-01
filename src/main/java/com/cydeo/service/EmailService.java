package com.cydeo.service;


public interface EmailService {

    void sendSimpleMessage(String sendTo, String subject, String message);

    void sendMessageWithAttachment(String sendTo, String subject, String message);
    void sendHtmlMessageWithImage(String sendTo, String subject, String message);
}

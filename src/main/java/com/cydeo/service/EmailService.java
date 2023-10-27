package com.cydeo.service;

public interface EmailService {
    public void sendSimpleMessage(String sendTo, String subject, String message);
}

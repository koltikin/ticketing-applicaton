package com.cydeo.service;

public interface EmailService {
    public void sendUserVerificationEmail(String sendTo, String subject, String message);
}

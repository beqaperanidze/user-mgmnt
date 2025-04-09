package com.usermgmnt.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendRegistrationConfirmationEmail(String to, String username) throws MessagingException;
    void sendWelcomeEmail(String to, String username) throws MessagingException;
    void sendPasswordResetEmail(String to, String username) throws MessagingException;
}

package com.usermgmnt.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

/**
 * Service interface for handling email-related operations.
 */
@Service
public interface EmailService {

    /**
     * Sends a registration confirmation email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param username the username of the recipient
     * @throws MessagingException if an error occurs while sending the email
     */
    void sendRegistrationConfirmationEmail(String to, String username) throws MessagingException;

    /**
     * Sends a welcome email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param username the username of the recipient
     * @throws MessagingException if an error occurs while sending the email
     */
    void sendWelcomeEmail(String to, String username) throws MessagingException;

    /**
     * Sends a password reset email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param username the username of the recipient
     * @throws MessagingException if an error occurs while sending the email
     */
    void sendPasswordResetEmail(String to, String username) throws MessagingException;
}
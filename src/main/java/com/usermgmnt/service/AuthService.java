package com.usermgmnt.service;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

/**
 * Service interface for handling authentication-related operations.
 */
@Service
public interface AuthService {

    /**
     * Registers a new user in the system.
     *
     * @param userRegistrationDTO the data transfer object containing user registration details
     * @return a {@link UserDTO} object containing the registered user's details
     */
    UserDTO register(UserRegistrationDTO userRegistrationDTO);

    /**
     * Authenticates a user and generates a login token.
     *
     * @param email the email address of the user
     * @param password the password of the user
     * @return a string representing the login token
     */
    String login(String email, String password);

    /**
     * Confirms a user's account using a token.
     *
     * @param token the confirmation token
     * @return a string indicating the confirmation status
     * @throws MessagingException if an error occurs while sending confirmation-related emails
     */
    String confirm(String token) throws MessagingException;
}
package com.usermgmnt.service;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserDTO register(UserRegistrationDTO userRegistrationDTO);

    String login(String email, String password);

    boolean confirm(String token) throws MessagingException;
}

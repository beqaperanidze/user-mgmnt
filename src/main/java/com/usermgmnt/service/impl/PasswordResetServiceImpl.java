package com.usermgmnt.service.impl;

import com.usermgmnt.exceptions.InvalidTokenException;
import com.usermgmnt.exceptions.UserNotFoundException;
import com.usermgmnt.model.User;
import com.usermgmnt.repository.UserRepository;
import com.usermgmnt.service.EmailService;
import com.usermgmnt.service.PasswordResetService;
import jakarta.mail.MessagingException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(EmailService emailService, UserRepository userRepository, RedisTemplate<String, String> redisTemplate, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found with email: %s".formatted(email)));
        try {
            emailService.sendPasswordResetEmail(email, user.getFirstName());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean confirmCode(String email, String token) {
        String storedCode = redisTemplate.opsForValue().get("password-reset:%s".formatted(email));
        return storedCode.equals(token);
    }

    @Override
    public String changePassword(String email, String token, String password) {
        if (!confirmCode(email, token)) {
            throw new InvalidTokenException("Invalid or expired password reset code");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found with email: %s".formatted(email)));

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        redisTemplate.delete("password-reset:%s".formatted(email));
        return "Password has been changed!";
    }
}

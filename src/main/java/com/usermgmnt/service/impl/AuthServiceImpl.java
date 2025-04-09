package com.usermgmnt.service.impl;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import com.usermgmnt.enums.Role;
import com.usermgmnt.exceptions.AuthenticationFailedException;
import com.usermgmnt.exceptions.UserNotFoundException;
import com.usermgmnt.mapper.UserMapper;
import com.usermgmnt.model.User;
import com.usermgmnt.repository.UserRepository;
import com.usermgmnt.security.JwtUtil;
import com.usermgmnt.service.AuthService;
import com.usermgmnt.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                           EmailService emailService, JwtUtil jwtUtil, UserMapper userMapper,
                           UserRepository userRepository, PasswordEncoder passwordEncoder,
                           StringRedisTemplate stringRedisTemplate, RedisTemplate<String, String> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public UserDTO register(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setAge(userRegistrationDTO.getAge());
        user.setRole(Role.USER);
        user.setApproved(true);

        String confirmationKey = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(confirmationKey, userRegistrationDTO.getEmail());
        try {
            emailService.sendRegistrationConfirmationEmail(userRegistrationDTO.getEmail(), userRegistrationDTO.getFirstName());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return jwtUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid email or password");
        }
    }

    @Override
    public boolean confirm(String token) throws MessagingException {
        String key = "confirmation:%s".formatted(token);
        String email = redisTemplate.opsForValue().get(key);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found with Email: %s".formatted(email)));
        user.setApproved(true);
        redisTemplate.delete(key);
        emailService.sendWelcomeEmail(email, user.getFirstName());
        return true;
    }
}

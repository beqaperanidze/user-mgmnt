package com.usermgmnt.service.impl;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import com.usermgmnt.enums.Role;
import com.usermgmnt.exceptions.AuthenticationFailedException;
import com.usermgmnt.mapper.UserMapper;
import com.usermgmnt.model.User;
import com.usermgmnt.repository.UserRepository;
import com.usermgmnt.security.JwtUtil;
import com.usermgmnt.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                           JwtUtil jwtUtil, UserMapper userMapper, UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}

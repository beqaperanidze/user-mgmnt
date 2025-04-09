package com.usermgmnt.controller;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import com.usermgmnt.service.AuthService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserDTO registeredUser = authService.register(registrationDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/confirm")
    public boolean confirm(@RequestParam("token") String token) {
        return authService.confirm(token);
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
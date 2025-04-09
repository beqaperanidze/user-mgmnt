package com.usermgmnt.controller;

import com.usermgmnt.service.PasswordResetService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgotPassword(@RequestParam("email") String email) {
        passwordResetService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Boolean> confirmCode(@RequestBody CodeConfirmationRequest request) {
        boolean confirmed = passwordResetService.confirmCode(request.getEmail(), request.getToken());
        return ResponseEntity.ok(confirmed);
    }

    @PostMapping("/change")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeRequest request) {
        String message = passwordResetService.changePassword(
                request.getEmail(),
                request.getToken(),
                request.getNewPassword()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Info", message)
                .build();
    }

    @Data
    public static class CodeConfirmationRequest {
        private String email;
        private String token;
    }

    @Data
    public static class PasswordChangeRequest {
        private String email;
        private String token;
        private String newPassword;
    }
}

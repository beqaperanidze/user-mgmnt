package com.usermgmnt.service;

public interface PasswordResetService {
    void forgotPassword(String email);

    boolean confirmCode(String email, String token);

    String changePassword(String email, String token, String password);
}

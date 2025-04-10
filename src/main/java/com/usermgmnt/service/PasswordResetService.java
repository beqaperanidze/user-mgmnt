package com.usermgmnt.service;

/**
 * Service interface for handling password reset operations.
 */
public interface PasswordResetService {

    /**
     * Initiates the password reset process by sending a reset code to the user's email.
     *
     * @param email the email address of the user who forgot their password
     */
    void forgotPassword(String email);

    /**
     * Confirms the reset code sent to the user's email.
     *
     * @param email the email address of the user
     * @param token the reset code/token to be confirmed
     * @return true if the code is valid and confirmed, false otherwise
     */
    boolean confirmCode(String email, String token);

    /**
     * Changes the user's password after confirming the reset code.
     *
     * @param email the email address of the user
     * @param token the reset code/token to validate the request
     * @param password the new password to be set for the user
     * @return a string indicating the status of the password change operation
     */
    String changePassword(String email, String token, String password);
}
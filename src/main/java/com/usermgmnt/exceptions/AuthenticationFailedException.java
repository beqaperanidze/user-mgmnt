package com.usermgmnt.exceptions;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String text) {
        super(text);
    }
}

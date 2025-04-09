package com.usermgmnt.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String text) {
        super(text);
    }
}

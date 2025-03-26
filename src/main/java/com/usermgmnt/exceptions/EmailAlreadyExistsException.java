package com.usermgmnt.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String text) {
        super(text);
    }
}

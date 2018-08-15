package com.clothshop.accountservice.exception;

public class UserNotFoundException extends EntityConstraintViolationException {

    public UserNotFoundException() {
        this("No user found");
    }

    public UserNotFoundException(String arg) {
        super(arg);
    }
}

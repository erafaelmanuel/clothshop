package com.clothshop.accountservice.exception;

public class UserNotFoundException extends EntityException {

    public UserNotFoundException() {
        this("No user found");
    }

    public UserNotFoundException(String arg) {
        super(arg);
    }
}

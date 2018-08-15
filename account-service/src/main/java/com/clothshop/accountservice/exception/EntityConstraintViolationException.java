package com.clothshop.accountservice.exception;

public class EntityConstraintViolationException extends RuntimeException {

    public EntityConstraintViolationException() {
        this("No message");
    }

    public EntityConstraintViolationException(String arg) {
        super(arg);
    }
}

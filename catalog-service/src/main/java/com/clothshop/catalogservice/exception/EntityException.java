package com.clothshop.catalogservice.exception;

public class EntityException extends RuntimeException {

    public EntityException() {
        this("No message");
    }

    public EntityException(String arg) {
        super(arg);
    }
}

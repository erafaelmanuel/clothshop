package com.clothshop.client.web.exception;

public class ResourceException extends RuntimeException {

    public ResourceException() {
        this("No message");
    }

    public ResourceException(String message) {
        super(message);
    }
}

package com.clothshop.catalogservice.exception;

public class CategoryNotFoundException extends EntityException {

    public CategoryNotFoundException() {
        this("No category found");
    }

    public CategoryNotFoundException(String arg) {
        super(arg);
    }
}

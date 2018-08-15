package com.clothshop.catalogservice.exception;

public class ItemNotFoundException extends EntityException {

    public ItemNotFoundException() {
        this("No item found");
    }

    public ItemNotFoundException(String arg) {
        super(arg);
    }
}

package com.clothshop.accountservice.exception;

public class RoleNotFoundException extends EntityException {

    public RoleNotFoundException() {
        this("No role found");
    }

    public RoleNotFoundException(String arg) {
        super(arg);
    }
}

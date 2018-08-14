package com.clothshop.accountservice.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message {

    private Timestamp timestamp;
    private int status;
    private String message;

    public Message(int status, String message) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
        this.message = message;
    }
}

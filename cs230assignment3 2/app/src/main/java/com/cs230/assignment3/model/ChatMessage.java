package com.cs230.assignment3.model;

import java.util.Date;

public class ChatMessage {

    private String sender;
    private String receiver;
    private long messageTime;
    private String message;

    public ChatMessage(String sender, String receiver,String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message =message;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}

package com.contact.project.helpers;

import lombok.*;


public class Message {


    private String content;

 
    @Builder.Default
    private MessageType type = MessageType.blue;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}

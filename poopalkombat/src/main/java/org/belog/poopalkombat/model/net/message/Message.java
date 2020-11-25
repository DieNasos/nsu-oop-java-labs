package org.belog.poopalkombat.model.net.message;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private String message;

    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    // getters
    public MessageType getType() { return type; }
    public String getMessage() { return message; }
}
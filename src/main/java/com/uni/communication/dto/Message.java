package com.uni.communication.dto;

import lombok.Data;

@Data
public class Message {
    private MessageType type;
    private String payload;
}

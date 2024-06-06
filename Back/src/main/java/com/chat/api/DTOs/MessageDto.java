package com.chat.api.DTOs;

public record MessageDto(
        String senderName,
        String receiverName,
        String message,
        String date,
        Status status
) {
    public enum Status {
        JOIN,
        MESSAGE,
        LEAVE
    }
}



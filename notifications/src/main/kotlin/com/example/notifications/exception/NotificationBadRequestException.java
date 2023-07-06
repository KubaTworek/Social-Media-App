package com.example.notifications.exception;

public class NotificationBadRequestException extends RuntimeException {
    public NotificationBadRequestException(String message) {
        super(message);
    }
}


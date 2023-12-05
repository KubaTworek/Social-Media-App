package pl.jakubtworek.notifications.exception;

public class NotificationBadRequestException extends RuntimeException {
    public NotificationBadRequestException(String message) {
        super(message);
    }
}


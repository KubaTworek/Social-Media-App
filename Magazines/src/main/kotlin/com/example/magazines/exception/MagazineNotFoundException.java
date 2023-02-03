package com.example.magazines.exception;

public class MagazineNotFoundException extends RuntimeException {
    public MagazineNotFoundException(String message) {
        super(message);
    }
}

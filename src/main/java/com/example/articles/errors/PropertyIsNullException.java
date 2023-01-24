package com.example.articles.errors;

public class PropertyIsNullException extends RuntimeException{
    public PropertyIsNullException(String message) {
        super(message);
    }
}

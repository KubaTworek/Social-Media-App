package com.example.RESTAPIarticle.errors;

public class PropertyIsNullException extends RuntimeException{
    public PropertyIsNullException(String message) {
        super(message);
    }
}

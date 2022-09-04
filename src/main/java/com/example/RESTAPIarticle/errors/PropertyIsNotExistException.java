package com.example.RESTAPIarticle.errors;

public class PropertyIsNotExistException extends RuntimeException{
    public PropertyIsNotExistException(String message) {
        super(message);
    }
}

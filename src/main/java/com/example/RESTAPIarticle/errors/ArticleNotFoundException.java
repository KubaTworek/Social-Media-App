package com.example.RESTAPIarticle.errors;

public class ArticleNotFoundException extends RuntimeException{

    public ArticleNotFoundException(String message) {
        super(message);
    }
}

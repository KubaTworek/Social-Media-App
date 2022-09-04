package com.example.RESTAPIarticle.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ArticleRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ArticleErrorResponse> handleException(ArticleNotFoundException exc){
        ArticleErrorResponse error = new ArticleErrorResponse();

        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(exc.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ArticleErrorResponse> handleException(PropertyIsNullException exc){
        ArticleErrorResponse error = new ArticleErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ArticleErrorResponse> handleException(PropertyIsNotExistException exc){
        ArticleErrorResponse error = new ArticleErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ArticleErrorResponse> handleHttpMediaTypeNotAcceptableException() {
        ArticleErrorResponse error = new ArticleErrorResponse();

        error.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessage("Acceptable content type:" + MediaType.APPLICATION_JSON_VALUE);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

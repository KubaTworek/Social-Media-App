package com.example.authors.exception

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ArticleExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException::class)
    fun handleException(exc: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotAcceptableException(): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.NOT_ACCEPTABLE.value(),
            "Acceptable content type: " + MediaType.APPLICATION_JSON_VALUE
        )
        return ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE)
    }
}

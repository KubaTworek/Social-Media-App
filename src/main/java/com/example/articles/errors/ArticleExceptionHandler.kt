package com.example.articles.errors

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ArticleRestExceptionHandler {

    @ExceptionHandler
    fun handleException(exc: ArticleNotFoundException): ResponseEntity<ArticleErrorResponse> {
        val error = ArticleErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotAcceptableException(): ResponseEntity<ArticleErrorResponse> {
        val error = ArticleErrorResponse(
            HttpStatus.NOT_ACCEPTABLE.value(),
            "Acceptable content type:" + MediaType.APPLICATION_JSON_VALUE
        )
        return ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE)
    }
}
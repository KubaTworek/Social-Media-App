package com.example.articles.exception

import org.springframework.http.*
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ArticleExceptionHandler {

    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFoundException(exc: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(exc: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
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

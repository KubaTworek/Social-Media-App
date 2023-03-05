package com.example.authorization.exception

import org.springframework.http.*
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class AuthorizationExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExistsException(exc: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(exc: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(WrongCredentialsException::class)
    fun handleWrongCredentialsException(exc: Exception): ResponseEntity<ErrorResponse> {
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

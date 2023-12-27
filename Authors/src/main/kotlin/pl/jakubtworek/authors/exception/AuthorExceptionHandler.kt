package pl.jakubtworek.authors.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class AuthorExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(AuthorExceptionHandler::class.java)

    @ExceptionHandler(AuthorNotFoundException::class)
    fun handleException(exc: Exception): ResponseEntity<ErrorResponse> {
        logger.error("AuthorNotFoundException: ${exc.message}")
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotAcceptableException(): ResponseEntity<ErrorResponse> {
        logger.error("HttpMediaTypeNotSupportedException: Acceptable content type: ${MediaType.APPLICATION_JSON_VALUE}")
        val error = ErrorResponse(
            HttpStatus.NOT_ACCEPTABLE.value(),
            "Acceptable content type: " + MediaType.APPLICATION_JSON_VALUE
        )
        return ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE)
    }
}

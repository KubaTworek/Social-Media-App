package pl.jakubtworek.notifications.exception

import org.springframework.http.*
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class AuthorExceptionHandler {

    @ExceptionHandler(pl.jakubtworek.notifications.exception.NotificationBadRequestException::class)
    fun handleException(exc: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
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

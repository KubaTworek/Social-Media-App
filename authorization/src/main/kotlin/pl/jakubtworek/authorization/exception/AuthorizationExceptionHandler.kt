package pl.jakubtworek.authorization.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class AuthorizationExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(AuthorizationExceptionHandler::class.java)

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExistsException(exc: Exception): ResponseEntity<ErrorResponse> {
        logger.error("UsernameAlreadyExistsException: ${exc.message}")
        val error = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(exc: Exception): ResponseEntity<ErrorResponse> {
        logger.error("UserNotFoundException: ${exc.message}")
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(WrongCredentialsException::class)
    fun handleWrongCredentialsException(exc: Exception): ResponseEntity<ErrorResponse> {
        logger.error("WrongCredentialsException: ${exc.message}")
        val error = ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
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

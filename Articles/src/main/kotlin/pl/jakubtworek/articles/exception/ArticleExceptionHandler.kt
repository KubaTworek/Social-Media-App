package pl.jakubtworek.articles.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.common.exception.UnauthorizedException

@ControllerAdvice
class ArticleExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(ArticleExceptionHandler::class.java)

    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFoundException(exc: Exception): ResponseEntity<ErrorResponse> {
        logger.error("ArticleNotFoundException: ${exc.message}")
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(exc: Exception): ResponseEntity<ErrorResponse> {
        logger.error("UnauthorizedException: ${exc.message}")
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

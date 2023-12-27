package pl.jakubtworek.common.client

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthorizationClientFallback : AuthorizationClient {
    override fun getUserDetails(jwt: String): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)
}

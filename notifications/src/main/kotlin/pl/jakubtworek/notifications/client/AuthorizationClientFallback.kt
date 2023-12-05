package pl.jakubtworek.notifications.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.*
import org.springframework.stereotype.Service

@Service
@Qualifier("AuthorizationClientFallback")
class AuthorizationClientFallback : pl.jakubtworek.notifications.client.AuthorizationClient {
    override fun getUserDetails(jwt: String): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)
}

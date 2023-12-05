package pl.jakubtworek.notifications.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(
    name = "authorization",
    url = "http://authorization:4000/api",
    fallback = pl.jakubtworek.notifications.client.AuthorizationClientFallback::class
)
@Qualifier("AuthorizationClient")
interface AuthorizationClient {
    @GetMapping("/user-info")
    fun getUserDetails(@RequestHeader("Authorization") jwt: String): ResponseEntity<String>
}

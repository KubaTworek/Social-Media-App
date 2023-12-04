package pl.jakubtworek.authorization.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.authorization.AbstractIT
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import kotlin.test.assertEquals

class AuthorizationControllerIT : AbstractIT() {

    @Test
    fun authorizationFlow() {
        val registerRequest = RegisterRequest("username", "password", "John", "Doe", "ROLE_USER")
        registerUser(registerRequest)
            .expectStatus().isCreated

        val loginRequest = LoginRequest("username", "password")
        val loginResponse = loginUser(loginRequest)
            .expectStatus().isAccepted
            .expectBody(LoginResponse::class.java)
        val jwt = loginResponse
            .returnResult().responseBody?.token

        val userDetailsResponse = getUserDetailsAfterLogin(jwt!!)
        val userDetails = userDetailsResponse.returnResult().responseBody
        assertEquals(1, userDetails?.authorId)
        assertEquals("John", userDetails?.firstName)
        assertEquals("Doe", userDetails?.lastName)
        assertEquals("username", userDetails?.username)
        assertEquals("ROLE_USER", userDetails?.role)
    }

    @Test
    fun wrongPassword() {
        val registerRequest = RegisterRequest("username", "password", "John", "Doe", "ROLE_USER")
        registerUser(registerRequest)
            .expectStatus().isCreated

        val loginRequest = LoginRequest("username", "wrong")
        loginUser(loginRequest)
            .expectStatus().isUnauthorized
    }

    @Test
    fun usernameExist() {
        val registerRequest1 = RegisterRequest("username", "password", "John", "Doe", "ROLE_USER")
        registerUser(registerRequest1)
            .expectStatus().isCreated

        val registerRequest2 = RegisterRequest("username", "password", "John", "Doe", "ROLE_USER")
        registerUser(registerRequest2)
            .expectStatus().isBadRequest
    }
}

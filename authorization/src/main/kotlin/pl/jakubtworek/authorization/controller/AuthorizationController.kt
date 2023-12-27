package pl.jakubtworek.authorization.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.authorization.service.AuthorizationService
import pl.jakubtworek.common.Constants.AUTHORIZATION_HEADER
import pl.jakubtworek.common.model.UserDetailsDTO

@RequestMapping("/api")
@RestController
class AuthorizationController(
    private val authorizationService: AuthorizationService
) {

    @PostMapping("/register", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(
        @RequestBody registerRequest: RegisterRequest
    ) = authorizationService.registerUser(registerRequest)

    @PostMapping("/login", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun loginUser(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<LoginResponse> = ResponseEntity.status(HttpStatus.OK)
        .body(authorizationService.loginUser(loginRequest))

    @GetMapping("/user-info", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getUserDetails(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String
    ): ResponseEntity<UserDetailsDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(authorizationService.getUserDetails(jwt))

    @DeleteMapping("/delete", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String
    ) = authorizationService.deleteUser(jwt)
}

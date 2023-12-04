package pl.jakubtworek.authorization.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.authorization.service.AuthorizationService

@RestController
@RequestMapping("/api")
class AuthorizationController(private val authorizationService: AuthorizationService) {

    // EXTERNAL
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody registerRequest: RegisterRequest) =
        authorizationService.registerUser(registerRequest)

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@RequestHeader("Authorization") jwt: String) =
        authorizationService.deleteUser(jwt)

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun loginUser(@RequestBody loginRequest: LoginRequest) =
        authorizationService.loginUser(loginRequest)

    // INTERNAL
    @GetMapping("/user-info")
    @ResponseStatus(HttpStatus.OK)
    fun getUserDetailsAfterLogin(@RequestHeader("Authorization") jwt: String) =
        authorizationService.getUserDetails(jwt)
}

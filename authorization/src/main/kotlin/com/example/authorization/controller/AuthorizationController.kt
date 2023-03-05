package com.example.authorization.controller

import com.example.authorization.service.AuthorizationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthorizationController(private val authorizationService: AuthorizationService) {

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

    @GetMapping("/user-info")
    @ResponseStatus(HttpStatus.OK)
    fun getUserDetailsAfterLogin(@RequestHeader("Authorization") jwt: String) =
        authorizationService.getUserDetails(jwt)
}

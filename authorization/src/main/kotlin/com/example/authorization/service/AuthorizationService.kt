package com.example.authorization.service

import com.example.authorization.controller.*

interface AuthorizationService {
    fun registerUser(registerRequest: RegisterRequest): UserResponse

    fun loginUser(loginRequest: LoginRequest): String

    fun getUserDetails(jwt: String): UserResponse
}

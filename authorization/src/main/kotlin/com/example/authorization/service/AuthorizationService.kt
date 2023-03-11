package com.example.authorization.service

import com.example.authorization.controller.*

interface AuthorizationService {
    fun registerUser(registerRequest: RegisterRequest)
    fun deleteUser(jwt: String)
    fun loginUser(loginRequest: LoginRequest): LoginResponse
    fun getUserDetails(jwt: String): UserResponse
}

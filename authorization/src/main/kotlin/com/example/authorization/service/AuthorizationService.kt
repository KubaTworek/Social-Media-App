package com.example.authorization.service

import com.example.authorization.controller.dto.LoginRequest
import com.example.authorization.controller.dto.LoginResponse
import com.example.authorization.controller.dto.RegisterRequest
import com.example.authorization.entity.UserDetailsDTO

interface AuthorizationService {
    fun registerUser(registerRequest: RegisterRequest)
    fun deleteUser(jwt: String)
    fun loginUser(loginRequest: LoginRequest): LoginResponse
    fun getUserDetails(jwt: String): UserDetailsDTO
}

package com.example.authorization.controller

data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)

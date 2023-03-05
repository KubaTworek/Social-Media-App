package com.example.authorization.controller

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String
)

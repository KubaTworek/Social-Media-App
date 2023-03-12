package com.example.authorization.controller

data class UserResponse(
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String
)

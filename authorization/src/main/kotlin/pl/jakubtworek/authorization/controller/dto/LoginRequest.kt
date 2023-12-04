package pl.jakubtworek.authorization.controller.dto

data class LoginRequest(
    val username: String,
    val password: String
)

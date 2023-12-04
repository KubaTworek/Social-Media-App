package pl.jakubtworek.authorization.controller.dto

data class LoginResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    val token: String,
    val tokenExpirationDate: Long
)

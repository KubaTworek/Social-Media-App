package pl.jakubtworek.authorization.controller.dto

data class LoginResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val following: Int,
    val followers: Int,
    val role: String,
    val token: String,
    val refreshToken: String,
    val tokenExpirationDate: Long,
    val refreshTokenExpirationDate: Long
)

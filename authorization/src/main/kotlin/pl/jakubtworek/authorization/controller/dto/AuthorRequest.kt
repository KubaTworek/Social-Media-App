package pl.jakubtworek.authorization.controller.dto

data class AuthorRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
)
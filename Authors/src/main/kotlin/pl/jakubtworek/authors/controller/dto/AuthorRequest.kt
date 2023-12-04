package pl.jakubtworek.authors.controller.dto

data class AuthorRequest(
    val firstName: String,
    val lastName: String,
    val username: String
)

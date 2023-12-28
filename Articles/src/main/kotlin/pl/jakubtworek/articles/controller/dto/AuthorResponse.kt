package pl.jakubtworek.articles.controller.dto

data class AuthorResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val isFollowed: Boolean
)

package pl.jakubtworek.notifications.model.dto

data class UserDetailsDTO(
    val authorId: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String
)

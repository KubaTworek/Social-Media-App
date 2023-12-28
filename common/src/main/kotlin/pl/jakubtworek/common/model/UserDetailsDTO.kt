package pl.jakubtworek.common.model

data class UserDetailsDTO(
    val authorId: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String
)

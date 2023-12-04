package pl.jakubtworek.authorization.entity

data class UserDetailsDTO(
    val authorId: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String
)

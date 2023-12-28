package pl.jakubtworek.common.model

data class UserDetailsDTO(
    val authorId: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val following: Int,
    val followers: Int,
    val role: String
)

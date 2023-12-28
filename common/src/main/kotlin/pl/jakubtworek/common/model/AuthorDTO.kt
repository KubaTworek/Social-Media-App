package pl.jakubtworek.common.model

data class AuthorDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val following: List<Int>,
    val followers: List<Int>
)

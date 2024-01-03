package pl.jakubtworek.notifications.controller.dto

data class AuthorWithActivityResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val following: List<Int>,
    val followers: List<Int>,
    val activities: List<ActivityResponse>
)
package pl.jakubtworek.notifications.controller.dto

data class NotificationResponse(
    val id: Int,
    val name: String,
    val message: String,
    val content: String
)

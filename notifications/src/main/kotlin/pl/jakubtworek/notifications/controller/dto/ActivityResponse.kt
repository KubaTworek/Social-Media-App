package pl.jakubtworek.notifications.controller.dto

import java.sql.Timestamp

data class ActivityResponse(
    val id: Int,
    val name: String,
    val targetName: String?,
    val message: String,
    val content: String?,
    val createAt: Timestamp
)
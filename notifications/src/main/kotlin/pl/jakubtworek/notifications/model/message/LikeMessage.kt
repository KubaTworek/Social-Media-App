package pl.jakubtworek.notifications.model.message

import java.sql.Timestamp

data class LikeMessage(
    val timestamp: Timestamp,
    val authorId: Int,
    val articleId: Int,
)

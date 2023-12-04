package pl.jakubtworek.articles.kafka.message

import java.sql.Timestamp

data class LikeMessage(
    val timestamp: Timestamp,
    val authorId: Int,
    val articleId: Int,
)
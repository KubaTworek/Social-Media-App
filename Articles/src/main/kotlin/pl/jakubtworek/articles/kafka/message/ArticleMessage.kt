package pl.jakubtworek.articles.kafka.message

import java.sql.Timestamp

data class ArticleMessage(
    val timestamp: Timestamp,
    val articleId: Int,
    val authorId: Int
)

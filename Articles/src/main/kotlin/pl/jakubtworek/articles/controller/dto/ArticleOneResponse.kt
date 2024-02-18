package pl.jakubtworek.articles.controller.dto

import java.sql.Timestamp

data class ArticleOneResponse(
    val id: Int,
    val text: String,
    val timestamp: Timestamp,
    val author: AuthorResponse,
    val likes: LikeInfoResponse,
    var comments: List<ArticleResponse>
)

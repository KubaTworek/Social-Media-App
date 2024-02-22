package pl.jakubtworek.articles.controller.dto

import java.sql.Timestamp

data class ArticleResponse(
    val id: Int,
    val text: String,
    val timestamp: Timestamp,
    val elapsed: String,
    val createDate: String,
    val author: AuthorResponse,
    val likes: LikeDetailsResponse,
    val numOfComments: Int
)

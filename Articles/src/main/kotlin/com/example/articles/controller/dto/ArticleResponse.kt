package com.example.articles.controller.dto

import java.sql.Timestamp

data class ArticleResponse(
    val id: Int,
    val text: String,
    val timestamp: Timestamp,
    val author: AuthorResponse,
    val likes: LikeInfoResponse
)

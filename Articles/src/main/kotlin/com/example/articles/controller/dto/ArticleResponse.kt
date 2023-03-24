package com.example.articles.controller.dto

import java.sql.Timestamp

data class ArticleResponse(
    val id: Int,
    val text: String,
    val timestamp: Timestamp,
    val author_firstName: String,
    val author_lastName: String,
    val author_username: String
)

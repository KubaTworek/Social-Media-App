package com.example.articles.model.dto

data class ArticleDTO(
    val id: Int,
    val date: String,
    val timestamp: String,
    val authorId: Int,
    val content: ArticleContentDTO
)
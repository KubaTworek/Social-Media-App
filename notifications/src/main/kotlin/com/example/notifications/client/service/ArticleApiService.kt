package com.example.notifications.client.service

import com.example.notifications.model.dto.ArticleDTO

interface ArticleApiService {
    fun getArticleById(articleId: Int): ArticleDTO
}
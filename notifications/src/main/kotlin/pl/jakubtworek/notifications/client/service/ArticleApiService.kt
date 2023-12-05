package pl.jakubtworek.notifications.client.service

import pl.jakubtworek.notifications.model.dto.ArticleDTO

interface ArticleApiService {
    fun getArticleById(articleId: Int): ArticleDTO
    fun getArticlesByAuthor(authorId: Int): List<ArticleDTO>
}
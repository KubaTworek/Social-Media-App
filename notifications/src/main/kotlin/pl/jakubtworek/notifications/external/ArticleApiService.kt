package pl.jakubtworek.notifications.external

import pl.jakubtworek.common.model.ArticleDTO

interface ArticleApiService {
    fun getArticleById(articleId: Int): ArticleDTO
    fun getArticlesByAuthor(authorId: Int): List<ArticleDTO>
}
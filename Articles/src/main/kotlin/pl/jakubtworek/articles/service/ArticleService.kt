package pl.jakubtworek.articles.service

import pl.jakubtworek.articles.controller.dto.ArticleOneResponse
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse
import pl.jakubtworek.common.model.ArticleDTO

interface ArticleService {
    fun getLatestArticles(page: Int, size: Int, jwt: String): List<ArticleResponse>
    fun getLatestFollowingArticles(page: Int, size: Int, jwt: String): List<ArticleResponse>
    fun getArticle(articleId: Int, jwt: String): ArticleOneResponse
    fun getArticlesByAuthorId(authorId: Int): List<ArticleDTO>
    fun getArticleById(articleId: Int): ArticleDTO
    fun saveArticle(request: ArticleRequest, jwt: String): ArticleResponse
    fun updateArticle(request: ArticleRequest, articleId: Int, jwt: String)
    fun handleLikeAction(articleId: Int, jwt: String): LikeResponse
    fun deleteArticleById(articleId: Int, jwt: String)
    fun deleteArticlesByAuthorId(authorId: Int)
}

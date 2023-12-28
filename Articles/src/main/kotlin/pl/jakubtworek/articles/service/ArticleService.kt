package pl.jakubtworek.articles.service

import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse
import pl.jakubtworek.common.model.ArticleDTO

interface ArticleService {
    fun findAllOrderByCreatedTimeDesc(page: Int, size: Int, jwt: String): List<ArticleResponse>
    fun findAllByAuthorId(authorId: Int): List<ArticleDTO>
    fun findById(articleId: Int): ArticleDTO
    fun save(theArticle: ArticleRequest, jwt: String)
    fun update(theArticle: ArticleRequest, articleId: Int, jwt: String)
    fun like(articleId: Int, jwt: String): LikeResponse
    fun deleteById(theId: Int, jwt: String)
    fun deleteByAuthorId(authorId: Int)
}

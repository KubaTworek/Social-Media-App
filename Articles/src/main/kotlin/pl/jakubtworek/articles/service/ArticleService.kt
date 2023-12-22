package pl.jakubtworek.articles.service

import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.common.model.ArticleDTO

interface ArticleService {
    fun findAllOrderByCreatedTimeDesc(page: Int, size: Int): List<ArticleResponse>
    fun findAllByAuthorId(authorId: Int): List<ArticleDTO>
    fun findById(articleId: Int): ArticleDTO
    fun save(theArticle: ArticleRequest, jwt: String)
    fun update(theArticle: ArticleRequest, articleId: Int, jwt: String)
    fun deleteById(theId: Int, jwt: String)
    fun deleteByAuthorId(authorId: Int)
}


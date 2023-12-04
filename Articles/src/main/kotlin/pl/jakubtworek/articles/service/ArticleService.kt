package pl.jakubtworek.articles.service

import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.model.dto.ArticleDTO

interface ArticleService {
    fun findAllOrderByCreatedTimeDesc(): List<ArticleResponse>
    fun findAllByAuthorId(authorId: Int): List<ArticleDTO>
    fun findById(articleId: Int): ArticleDTO
    fun save(theArticle: ArticleRequest, jwt: String)
    fun deleteById(theId: Int, jwt: String)
    fun deleteByAuthorId(authorId: Int)
}


package com.example.articles.service

import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.model.dto.ArticleDTO

interface ArticleService {
    fun findAllOrderByDateDesc(): List<ArticleResponse>
    fun findById(theId: Int): ArticleResponse
    fun findAllByAuthorId(authorId: Int): List<ArticleDTO>
    fun findAllByKeyword(theKeyword: String): List<ArticleResponse>
    fun save(theArticle: ArticleRequest)
    fun deleteById(theId: Int)
    fun deleteByAuthorId(authorId: Int)
}

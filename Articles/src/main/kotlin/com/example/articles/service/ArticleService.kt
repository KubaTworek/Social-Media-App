package com.example.articles.service

import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.model.Article
import java.util.*

interface ArticleService {
    fun findAllOrderByDateDesc(): List<ArticleResponse>
    fun findById(theId: Int): Optional<ArticleResponse>
    fun findAllByAuthorId(authorId: Int): List<Article>
    fun findAllByMagazineId(magazineId: Int): List<Article>
    fun findAllByKeyword(theKeyword: String): List<ArticleResponse>
    fun save(theArticle: ArticleRequest)
    fun deleteById(theId: Int)
}

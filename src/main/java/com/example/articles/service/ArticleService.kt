package com.example.articles.service

import com.example.articles.controller.article.ArticleRequest
import com.example.articles.entity.ArticlePost
import java.util.*

interface ArticleService {
    fun findAllOrderByDateDesc(): List<ArticlePost>
    fun findById(theId: Int): Optional<ArticlePost>
    fun findAllByKeyword(theKeyword: String): List<ArticlePost>
    fun save(theArticle: ArticleRequest)
    fun deleteById(theId: Int)
}
package com.example.articles.service

import com.example.articles.controller.ArticleRequest
import com.example.articles.entity.Article
import java.util.*

interface ArticleService {
    fun findAllOrderByDateDesc(): List<Article>
    fun findById(theId: Int): Optional<Article>
    fun findAllByKeyword(theKeyword: String): List<Article>
    fun save(theArticle: ArticleRequest)
    fun deleteById(theId: Int)
}
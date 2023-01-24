package com.example.articles.service

import com.example.articles.entity.Article

interface ArticleService {
    fun findAllOrderByDateDesc(): List<Article>
    fun findById(theId: Int): Article
    fun findAllByKeyword(theKeyword: String): List<Article>
    fun save(theArticle: Article): Article
    fun deleteById(theId: Int)
}
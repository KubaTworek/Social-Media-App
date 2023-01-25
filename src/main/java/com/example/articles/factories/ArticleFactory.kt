package com.example.articles.factories

import com.example.articles.controller.ArticleRequest
import com.example.articles.entity.Article
import com.example.articles.entity.ArticleContent
import com.example.articles.entity.Author
import com.example.articles.entity.Magazine
import org.springframework.stereotype.Component
import java.time.LocalDateTime

import java.time.format.DateTimeFormatter




@Component
class ArticleFactory {
    fun createArticle(articleRequest: ArticleRequest, author: Author, magazine: Magazine, content: ArticleContent): Article {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val now = LocalDateTime.now()

        return Article(
            0,
            dtf.format(now),
            System.currentTimeMillis(),
            author,
            magazine,
            content
        )
    }
}
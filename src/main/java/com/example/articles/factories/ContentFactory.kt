package com.example.articles.factories

import com.example.articles.entity.ArticleContent
import org.springframework.stereotype.Component

@Component
class ContentFactory {
    fun createContent(title: String, text: String): ArticleContent {
        return ArticleContent(
            0,
            title,
            text,
            null
        )
    }
}
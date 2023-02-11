package com.example.articles.factories

import com.example.articles.model.entity.ArticleContent
import org.springframework.stereotype.Component

@Component
class ContentFactory {
    fun createContent(title: String, text: String): ArticleContent =
        ArticleContent(
            0,
            title,
            text,
            null
        )
}
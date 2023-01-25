package com.example.articles.factories

import com.example.articles.entity.Article
import com.example.articles.entity.ArticleContent
import com.example.articles.entity.Author
import org.springframework.stereotype.Component
import java.util.*
import java.util.Collections.emptyList

@Component
class AuthorFactory {
    fun createAuthor(firstName: String, lastName: String): Author {

        return Author(
            0,
            firstName,
            lastName,
            emptyList()
        )
    }
}
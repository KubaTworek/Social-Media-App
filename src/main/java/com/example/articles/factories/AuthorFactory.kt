package com.example.articles.factories

import com.example.articles.controller.author.AuthorRequest
import com.example.articles.entity.AuthorPost
import org.springframework.stereotype.Component
import java.util.Collections.emptyList

@Component
class AuthorFactory {
    fun createAuthor(firstName: String, lastName: String): AuthorPost {

        return AuthorPost(
            0,
            firstName,
            lastName,
            emptyList()
        )
    }

    fun createAuthor(authorRequest: AuthorRequest): AuthorPost {

        return AuthorPost(
            0,
            authorRequest.firstName,
            authorRequest.lastName,
            emptyList()
        )
    }
}
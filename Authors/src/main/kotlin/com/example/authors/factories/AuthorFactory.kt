package com.example.authors.factories

import com.example.authors.controller.AuthorRequest
import com.example.authors.model.Author
import org.springframework.stereotype.Component
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

    fun createAuthor(authorRequest: AuthorRequest): Author {

        return Author(
            0,
            authorRequest.firstName,
            authorRequest.lastName,
            emptyList()
        )
    }
}
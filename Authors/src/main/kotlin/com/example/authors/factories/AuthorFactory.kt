package com.example.authors.factories

import com.example.authors.client.ArticleClient
import com.example.authors.controller.AuthorRequest
import com.example.authors.controller.AuthorResponse
import com.example.authors.model.dto.ArticleDTO
import com.example.authors.model.entity.Author
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AuthorFactory(
    private val articleClient: ArticleClient,
    private val objectMapper: ObjectMapper
) {
    fun createAuthor(authorRequest: AuthorRequest): Author {
        return Author(
            0,
            authorRequest.firstName,
            authorRequest.lastName,
            authorRequest.username
        )
    }

    fun createResponse(theAuthor: Author): AuthorResponse {
        val articles = getArticlesByAuthor(theAuthor.id)
        val articleIds = articles.map { it.id }.toList()

        return AuthorResponse(
            theAuthor.id,
            theAuthor.firstName,
            theAuthor.lastName,
            theAuthor.username,
            articleIds
        )
    }

    private fun getArticlesByAuthor(authorId: Int): List<ArticleDTO> {
        val response = articleClient.getArticlesByAuthor(authorId)
        return deserializeArticles(response)
    }

    private fun deserializeArticles(response: ResponseEntity<String>): List<ArticleDTO> {
        return objectMapper.readValue(
            response.body,
            objectMapper.typeFactory.constructCollectionType(List::class.java, ArticleDTO::class.java)
        )
    }
}
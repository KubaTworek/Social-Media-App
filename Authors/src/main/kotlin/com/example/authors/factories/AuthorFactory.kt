package com.example.authors.factories

import com.example.authors.client.ArticleClient
import com.example.authors.controller.AuthorRequest
import com.example.authors.controller.AuthorResponse
import com.example.authors.model.Article
import com.example.authors.model.Author
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AuthorFactory(
    private val articleClient: ArticleClient
) {
    fun createAuthor(authorRequest: AuthorRequest): Author {
        return Author(
            0,
            authorRequest.firstName,
            authorRequest.lastName
        )
    }

    fun createResponse(theAuthor: Author): AuthorResponse{
        val articles = deserializeArticles(getArticles(theAuthor.id))
        val titles = articles.map{ it.content.title}.toList()

        return AuthorResponse(
            theAuthor.id,
            theAuthor.firstName,
            theAuthor.lastName,
            titles
        )
    }

    private fun getArticles(authorId: Int): ResponseEntity<String> {
        return articleClient.getArticlesByAuthor(authorId)
    }

    private fun deserializeArticles(response: ResponseEntity<String>): List<Article> {
        val objectMapper = ObjectMapper()
        return if(response.body == null){
            emptyList()
        } else {
            objectMapper.readValue(response.body, objectMapper.typeFactory.constructCollectionType(List::class.java, Article::class.java))
        }
    }
}
package com.example.articles.factories

import com.example.articles.clients.AuthorClient
import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.model.dto.AuthorDTO
import com.example.articles.model.entity.Article
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class ArticleFactory(
    private val authorClient: AuthorClient,
    private val contentFactory: ContentFactory,
    private val objectMapper: ObjectMapper
) {
    fun createArticle(request: ArticleRequest): Article {
        val content = contentFactory.createContent(request.title, request.text)

        return Article(
            0,
            getCurrentDate(),
            System.currentTimeMillis().toString(),
            request.authorId,
            content
        )
    }

    fun createResponse(theArticle: Article): ArticleResponse {
        val author = deserializeAuthor(getAuthor(theArticle.authorId))

        return ArticleResponse(
            0,
            theArticle.content.title,
            theArticle.content.text,
            author.firstName,
            author.lastName
        )
    }

    private fun getAuthor(authorId: Int): ResponseEntity<String> =
        authorClient.getAuthor(authorId)

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)

    private fun getCurrentDate(): String {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return dateFormatter.format(LocalDateTime.now())
    }
}
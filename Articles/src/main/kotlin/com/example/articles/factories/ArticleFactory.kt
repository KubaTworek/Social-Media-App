package com.example.articles.factories

import com.example.articles.clients.AuthorClient
import com.example.articles.clients.MagazineClient
import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.AuthorRequest
import com.example.articles.controller.MagazineRequest
import com.example.articles.model.Article
import com.example.articles.model.Author
import com.example.articles.model.Magazine
import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class ArticleFactory(
    private val authorClient: AuthorClient,
    private val magazineClient: MagazineClient,
    private val contentFactory: ContentFactory,
) {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    fun createArticle(request: ArticleRequest): Article {
        val author = deserializeAuthor(getOrCreateAuthor(request))
        val magazine = deserializeMagazine(getOrCreateMagazine(request))
        val content = contentFactory.createContent(request.title, request.text)

        return Article(
            0,
            getCurrentDate(),
            System.currentTimeMillis().toString(),
            author,
            magazine,
            content
        )
    }

    private fun getOrCreateAuthor(request: ArticleRequest): ResponseEntity<String> {
        return try {
            authorClient.getAuthor(request.author_firstName, request.author_lastName)
        } catch (ex: FeignException) {
            authorClient.saveAuthor(AuthorRequest(request.author_firstName, request.author_lastName))
        }
    }

    private fun getOrCreateMagazine(request: ArticleRequest): ResponseEntity<String> {
        return try {
            magazineClient.getMagazine(request.magazine)
        } catch (ex: FeignException) {
            magazineClient.saveMagazine(MagazineRequest(request.magazine, "test"))
        }
    }

    private fun getCurrentDate(): String {
        return dateFormatter.format(LocalDateTime.now())
    }

    private fun deserializeAuthor(response: ResponseEntity<String>): Author {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(response.body, Author::class.java)
    }

    private fun deserializeMagazine(response: ResponseEntity<String>): Magazine {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(response.body, Magazine::class.java)
    }
}
package com.example.notifications.client.service

import com.example.notifications.client.*
import com.example.notifications.model.dto.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ArticleApiServiceImpl(
    @Qualifier("ArticleClient") private val articleClient: ArticleClient,
    private val objectMapper: ObjectMapper
) : ArticleApiService {
    override fun getArticleById(articleId: Int): ArticleDTO {
        val response = articleClient.getArticleById(articleId)
        return deserializeArticle(response)
    }

    override fun getArticlesByAuthor(authorId: Int): List<ArticleDTO> {
        val response = articleClient.getArticlesByAuthor(authorId)
        return deserializeArticles(response)
    }

    private fun deserializeArticle(response: ResponseEntity<String>): ArticleDTO =
        objectMapper.readValue(response.body, ArticleDTO::class.java)

    private fun deserializeArticles(response: ResponseEntity<String>): List<ArticleDTO> =
        objectMapper.readValue(response.body, object : TypeReference<List<ArticleDTO>>() {})
}

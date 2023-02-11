package com.example.magazines.factories

import com.example.magazines.client.ArticleClient
import com.example.magazines.controller.MagazineRequest
import com.example.magazines.controller.MagazineResponse
import com.example.magazines.model.Article
import com.example.magazines.model.Magazine
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class MagazineFactory(
    private val articleClient: ArticleClient
) {
    fun createMagazine(magazineRequest: MagazineRequest): Magazine {
        return Magazine(
            0,
            magazineRequest.magazineName
        )
    }

    fun createResponse(theMagazine: Magazine): MagazineResponse {
        val articles = deserializeArticles(getArticles(theMagazine.id))
        val titles = articles.map { it.content.title }.toList()

        return MagazineResponse(
            theMagazine.id,
            theMagazine.name,
            titles
        )
    }

    private fun getArticles(magazineId: Int): ResponseEntity<String> {
        return articleClient.getArticlesByMagazine(magazineId)
    }

    private fun deserializeArticles(response: ResponseEntity<String>): List<Article> {
        val objectMapper = ObjectMapper()
        return if (response.body == null) {
            emptyList()
        } else {
            objectMapper.readValue(
                response.body,
                objectMapper.typeFactory.constructCollectionType(List::class.java, Article::class.java)
            )
        }
    }
}
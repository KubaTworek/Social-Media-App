package com.example.magazines.factories

import com.example.magazines.client.ArticleClient
import com.example.magazines.controller.MagazineRequest
import com.example.magazines.controller.MagazineResponse
import com.example.magazines.model.entity.Magazine
import com.example.magazines.model.dto.ArticleDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class MagazineFactory(
    private val articleClient: ArticleClient,
    private val objectMapper: ObjectMapper
) {
    fun createMagazine(magazineRequest: MagazineRequest): Magazine {
        return Magazine(
            0,
            magazineRequest.magazineName
        )
    }

    fun createResponse(theMagazine: Magazine): MagazineResponse {
        val articles = getArticles(theMagazine.id)
        val articleTitles = articles.map { it.content.title }.toList()

        return MagazineResponse(
            theMagazine.id,
            theMagazine.name,
            articleTitles
        )
    }

    private fun getArticles(magazineId: Int): List<ArticleDTO> {
        val response = articleClient.getArticlesByMagazine(magazineId)
        return deserializeArticles(response)
    }

    private fun deserializeArticles(response: ResponseEntity<String>): List<ArticleDTO> {
        return if (response.body == "") {
            emptyList()
        } else {
            objectMapper.readValue(
                response.body,
                objectMapper.typeFactory.constructCollectionType(List::class.java, ArticleDTO::class.java)
            )
        }
    }
}
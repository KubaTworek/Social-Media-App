package pl.jakubtworek.articles.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.articles.AbstractIT
import pl.jakubtworek.articles.controller.dto.ArticleCreateRequest
import pl.jakubtworek.articles.controller.dto.ArticleUpdateRequest
import kotlin.test.assertEquals

class UpdateArticleTest : AbstractIT() {

    @Test
    fun shouldUpdateArticle() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id
        val updateRequest = ArticleUpdateRequest("Edited Test", createdArticleId!!)

        // When
        updateArticle(updateRequest, "user-jwt")

        // Then
        val updatedArticle = getArticleDetailsById(createdArticleId, "user-jwt").returnResult().responseBody
        assertEquals("Edited Test", updatedArticle?.text)

    }

    @Test
    fun shouldUpdateArticle_ByAdmin() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id
        val updateRequest = ArticleUpdateRequest("Edited Test", createdArticleId!!)

        // When
        val response = updateArticleAndReturnError(updateRequest, "admin-jwt")

        // Then
        assertEquals(401, response.returnResult().responseBody?.status)
        assertEquals("You are not authorized to update this article!", response.returnResult().responseBody?.message)
    }

    @Test
    fun shouldThrowException_whenNotExistArticle() {
        // Given
        val request = ArticleUpdateRequest("Edited Test", 999)

        // When
        val response = updateArticleAndReturnError(request, "user-jwt")

        // Then
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }

    @Test
    fun shouldNotUpdateArticle_ByNotPermittedUser() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id
        val updateRequest = ArticleUpdateRequest("Edited Test", createdArticleId!!)

        // When
        val response = updateArticleAndReturnError(updateRequest, "another-user-jwt")

        // Then
        assertEquals(401, response.returnResult().responseBody?.status)
        assertEquals("You are not authorized to update this article!", response.returnResult().responseBody?.message)
    }
}

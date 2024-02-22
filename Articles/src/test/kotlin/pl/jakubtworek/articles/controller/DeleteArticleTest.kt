package pl.jakubtworek.articles.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.articles.AbstractIT
import pl.jakubtworek.articles.controller.dto.ArticleCreateRequest
import kotlin.test.assertEquals

class DeleteArticleTest : AbstractIT() {

    @Test
    fun shouldDeleteArticle() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id

        // When
        deleteArticleById(createdArticleId!!, "user-jwt")

        // Then
        val response = getArticleDetailsAndReturnError(createdArticleId, "user-jwt")
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }

    @Test
    fun shouldDeleteComment() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id
        val commentRequest = ArticleCreateRequest("Example Comment", createdArticleId)
        val commentId = saveArticle(commentRequest, "user-jwt").returnResult().responseBody?.id

        // When
        deleteArticleById(commentId!!, "user-jwt")

        // Then
        getArticleDetailsById(createdArticleId!!, "user-jwt")
        val response = getArticleDetailsAndReturnError(commentId, "user-jwt")
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)

    }

    @Test
    fun shouldDeleteArticleWithComment() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id
        val commentRequest = ArticleCreateRequest("Example Comment", createdArticleId)
        val commentId = saveArticle(commentRequest, "user-jwt").returnResult().responseBody?.id

        // When
        deleteArticleById(createdArticleId!!, "user-jwt")

        // Then
        val response1 = getArticleDetailsAndReturnError(createdArticleId, "user-jwt")
        assertEquals(404, response1.returnResult().responseBody?.status)
        assertEquals("Article not found", response1.returnResult().responseBody?.message)
        val response2 = getArticleDetailsAndReturnError(commentId!!, "user-jwt")
        assertEquals(404, response2.returnResult().responseBody?.status)
        assertEquals("Article not found", response2.returnResult().responseBody?.message)
    }

    @Test
    fun shouldDeleteArticleWithLike() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id
        likeArticle(createdArticleId!!, "user-jwt")

        // When
        deleteArticleById(createdArticleId, "user-jwt")

        // Then
        val response = getArticleDetailsAndReturnError(createdArticleId, "user-jwt")
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }

    @Test
    fun shouldDeleteArticle_byAnotherUser() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id

        // When
        val response = deleteArticleByIdAndReturnError(createdArticleId!!, "another-user-jwt")

        // Then
        assertEquals(401, response.returnResult().responseBody?.status)
        assertEquals("You are not authorized to delete this article!", response.returnResult().responseBody?.message)
    }

    @Test
    fun shouldDeleteArticle_byAdmin() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id

        // When
        deleteArticleById(createdArticleId!!, "admin-jwt")

        // Then
        val response = getArticleDetailsAndReturnError(createdArticleId, "user-jwt")
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }

    @Test
    fun shouldDeleteArticleByAuthorId() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id

        // When
        deleteArticlesByAuthorId(2)

        // Then
        val response = getArticleDetailsAndReturnError(createdArticleId!!, "user-jwt")
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }
}

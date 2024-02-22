package pl.jakubtworek.articles.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.articles.AbstractIT
import pl.jakubtworek.articles.controller.dto.ArticleCreateRequest
import kotlin.test.assertEquals

class LikeArticleTest : AbstractIT() {

    @Test
    fun shouldLikeArticle() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id

        // When
        val response = likeArticle(createdArticleId!!, "user-jwt")

        // Then
        val like = response.returnResult().responseBody
        assertEquals("like", like?.status)
        val likedArticle = getArticleDetailsById(createdArticleId, "user-jwt")
        assertEquals(1, likedArticle.returnResult().responseBody?.likes?.users?.size)
        assertEquals("FirstName LastName", likedArticle.returnResult().responseBody?.likes?.users?.get(0))
    }

    @Test
    fun shouldLikeAndDislikeArticle() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)
        val createdArticleId = saveArticle(request, "user-jwt").returnResult().responseBody?.id

        // When
        val responseLike = likeArticle(createdArticleId!!, "user-jwt")
        val responseDislike = likeArticle(createdArticleId, "user-jwt")

        // Then
        val like = responseLike.returnResult().responseBody
        assertEquals("like", like?.status)
        val dislike = responseDislike.returnResult().responseBody
        assertEquals("dislike", dislike?.status)
        val likedArticle = getArticleDetailsById(createdArticleId, "user-jwt")
        assertEquals(0, likedArticle.returnResult().responseBody?.likes?.users?.size)
    }

    @Test
    fun shouldThrowException_whenArticleNotExist() {
        // When
        val response = likeArticleAndReturnError(999, "user-jwt")

        // Then
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }
}

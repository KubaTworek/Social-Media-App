package pl.jakubtworek.articles.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.articles.AbstractIT
import pl.jakubtworek.articles.controller.dto.ArticleCreateRequest
import kotlin.test.assertEquals

class CreateArticleTest : AbstractIT() {
    @Test
    fun shouldCreateArticle() {
        // Given
        val request = ArticleCreateRequest("Example Test", null)

        // When
        val response = saveArticle(request, "user-jwt")

        // Then
        val createdArticle = response.returnResult().responseBody
        assertEquals("Example Test", createdArticle?.text)
        assertEquals("FirstName", createdArticle?.author?.firstName)
        assertEquals("LastName", createdArticle?.author?.lastName)
        assertEquals("Username", createdArticle?.author?.username)
        assertEquals(0, createdArticle?.likes?.users?.size)
        assertEquals(0, createdArticle?.numOfComments)
    }

    @Test
    fun shouldCreateComment() {
        // Given
        val articleRequest = ArticleCreateRequest("Example Article", null)
        val articleId = saveArticle(articleRequest, "user-jwt").returnResult().responseBody?.id
        val commentOneRequest = ArticleCreateRequest("Example Comment 1", articleId)

        // When
        val firstCommentId = saveArticle(commentOneRequest, "user-jwt").returnResult().responseBody?.id
        val commentTwoRequest = ArticleCreateRequest("Example Comment 2", firstCommentId)
        saveArticle(commentTwoRequest, "user-jwt")

        // Then
        val articleFetched = getArticleDetailsById(articleId!!, "user-jwt").returnResult().responseBody
        assertEquals(1, articleFetched?.comments?.size)
        assertEquals("Example Comment 1", articleFetched?.comments?.get(0)?.text)
        assertEquals("FirstName", articleFetched?.comments?.get(0)?.author?.firstName)
        assertEquals("LastName", articleFetched?.comments?.get(0)?.author?.lastName)
        assertEquals("Username", articleFetched?.comments?.get(0)?.author?.username)
        assertEquals(0, articleFetched?.comments?.get(0)?.likes?.users?.size)
        assertEquals(1, articleFetched?.comments?.get(0)?.numOfComments)
    }

    @Test
    fun testTooMuchArticle() {
        /*
                // Given
                val request = ArticleRequest("x".repeat(5000), null)

                // When
                val response = saveArticleAnd(request, "user-jwt")

                // Then
                assertEquals("Text length must be between 1 and 4000 characters", response.returnResult().responseBody?.message)
        */
    }

    @Test
    fun shouldThrowException_whenCommentNotExistArticle() {
        // Given
        val commentOneRequest = ArticleCreateRequest("Example Comment 1", 999)

        // When
        val response = saveArticleAndReturnError(commentOneRequest, "user-jwt")

        // Then
        assertEquals(404, response.returnResult().responseBody?.status)
        assertEquals("Article not found", response.returnResult().responseBody?.message)
    }
}

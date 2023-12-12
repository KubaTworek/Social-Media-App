package pl.jakubtworek.articles.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.articles.AbstractIT
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import java.time.LocalDate
import kotlin.test.assertEquals

class ArticleControllerIT : AbstractIT() {

    // EXTERNAL

    @Test
    fun testGetArticles() {
        // When
        val response = getArticles(2)

        // Then
        val articles = response.returnResult().responseBody
        assertEquals("Example Content 2", articles?.get(0)?.text)
        assertEquals("FirstName", articles?.get(0)?.author?.firstName)
        assertEquals("LastName", articles?.get(0)?.author?.lastName)
        assertEquals("Username", articles?.get(0)?.author?.username)
        assertEquals("Example Content 1", articles?.get(1)?.text)
        assertEquals("FirstName", articles?.get(1)?.author?.firstName)
        assertEquals("LastName", articles?.get(1)?.author?.lastName)
        assertEquals("Username", articles?.get(1)?.author?.username)
    }

    @Test
    fun testSaveArticle() {
        // Given
        val articleRequest = ArticleRequest("New content")

        // When
        createArticle(articleRequest)

        // Then
        val response = getArticles(3)
        val articles = response.returnResult().responseBody
        assertEquals("New content", articles?.get(0)?.text)
        assertEquals("FirstName", articles?.get(0)?.author?.firstName)
        assertEquals("LastName", articles?.get(0)?.author?.lastName)
        assertEquals("Username", articles?.get(0)?.author?.username)
    }

    @Test
    fun testUpdateArticle() {
        // Given
        val articleRequest = ArticleRequest("New content")

        // When
        createArticle(articleRequest)

        // Then
        val response = getArticles(3)
        val articles = response.returnResult().responseBody
        assertEquals("New content", articles?.get(0)?.text)
        assertEquals("FirstName", articles?.get(0)?.author?.firstName)
        assertEquals("LastName", articles?.get(0)?.author?.lastName)
        assertEquals("Username", articles?.get(0)?.author?.username)

        // UDPATED
        // Given
        val articleRequest2 = ArticleRequest("Updated content")

        // When
        updateArticle(articleRequest2, articles!![0].id)

        // Then
        val response2 = getArticles(3)
        val articles2 = response2.returnResult().responseBody
        assertEquals("Updated content", articles2?.get(0)?.text)
        assertEquals("FirstName", articles2?.get(0)?.author?.firstName)
        assertEquals("LastName", articles2?.get(0)?.author?.lastName)
        assertEquals("Username", articles2?.get(0)?.author?.username)
    }

    @Test
    fun testDeleteArticle() {
        // Given
        val articles = getArticles(2)
        val articleId = articles.returnResult().responseBody?.firstOrNull()?.id

        // When
        deleteArticleByArticleId(articleId!!)

        // Then
        getArticles(1)
    }

    @Test
    fun testDeleteLikedArticle() {
        // Given
        val articles = getArticles(2)
        val articleId = articles.returnResult().responseBody?.firstOrNull()?.id

        // When
        likeArticle(articleId!!)
        deleteArticleByArticleId(articleId)

        // Then
        getArticles(1)
    }

    // INTERNAL
    @Test
    fun testGetArticleById() {
        // Given
        val articles = getArticles(2)
        val articlesId = articles.returnResult().responseBody?.map { it.id }

        // When
        articlesId?.forEach { id ->
            getArticleById(id)
        }
    }

    @Test
    fun testGetArticlesByAuthor() {
        // Given
        val authorId = 1

        // When
        val response = getArticlesByAuthorId(authorId, 2)

        // Then
        val articles = response.returnResult().responseBody
        assertEquals("Example Content 1", articles?.get(0)?.text)
        assertEquals(1, articles?.get(0)?.authorId)
        assertEquals(LocalDate.now().toString(), articles?.get(0)?.date)
        assertEquals("Example Content 2", articles?.get(1)?.text)
        assertEquals(1, articles?.get(1)?.authorId)
        assertEquals(LocalDate.now().toString(), articles?.get(1)?.date)
    }

    @Test
    fun testDeleteArticlesByAuthorId() {
        // Given
        val authorId = 1

        // When
        deleteArticleByAuthorId(authorId)

        // Then
        getArticles(0)
    }
}

package com.example.articles.controller

import com.example.articles.AbstractIT
import com.example.articles.controller.dto.ArticleRequest
import org.junit.jupiter.api.Test
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
        assertEquals("FirstName", articles?.get(0)?.author_firstName)
        assertEquals("LastName", articles?.get(0)?.author_lastName)
        assertEquals("Username", articles?.get(0)?.author_username)
        assertEquals(0, articles?.get(0)?.numOfLikes)
        assertEquals("Example Content 1", articles?.get(1)?.text)
        assertEquals("FirstName", articles?.get(1)?.author_firstName)
        assertEquals("LastName", articles?.get(1)?.author_lastName)
        assertEquals("Username", articles?.get(1)?.author_username)
        assertEquals(0, articles?.get(1)?.numOfLikes)
    }

    @Test
    fun testGetArticlesByKeyword() {
        // Given
        val keyword = "1"

        // When
        val response = getArticlesByKeyword(keyword, 1)

        // Then
        val articles = response.returnResult().responseBody
        assertEquals("Example Content 1", articles?.get(0)?.text)
        assertEquals("FirstName", articles?.get(0)?.author_firstName)
        assertEquals("LastName", articles?.get(0)?.author_lastName)
        assertEquals("Username", articles?.get(0)?.author_username)
        assertEquals(0, articles?.get(0)?.numOfLikes)
    }

    @Test
    fun testSaveArticle() {
        // Given
        val articleRequest = ArticleRequest("New Article", "New content")

        // When
        createArticle(articleRequest)

        // Then
        val response = getArticles(3)
        val articles = response.returnResult().responseBody
        assertEquals("New content", articles?.get(0)?.text)
        assertEquals("FirstName", articles?.get(0)?.author_firstName)
        assertEquals("LastName", articles?.get(0)?.author_lastName)
        assertEquals("Username", articles?.get(0)?.author_username)
        assertEquals(0, articles?.get(0)?.numOfLikes)
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
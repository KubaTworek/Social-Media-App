package pl.jakubtworek.authors.controller

import pl.jakubtworek.authors.AbstractIT
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AuthorControllerIT : AbstractIT() {

    @Test
    fun testSaveAuthor() {
        // Given
        val authorRequest = AuthorRequest("Adam", "Smith", "adamsmith")

        // When
        createAuthor(authorRequest)

        // Then
        val response = getAuthorByUsername("adamsmith")
        val author = response.returnResult().responseBody
        assertEquals("Adam", author?.firstName)
        assertEquals("Smith", author?.lastName)
        assertEquals("adamsmith", author?.username)
    }

    @Test
    fun testGetAuthorById() {
        // Given
        val response1 = getAuthorByUsername("johndoe")
        val authorId = response1.returnResult().responseBody?.id

        // When
        val response = getAuthorById(authorId!!)

        // Then
        val author = response.returnResult().responseBody
        assertEquals("John", author?.firstName)
        assertEquals("Doe", author?.lastName)
        assertEquals("johndoe", author?.username)
    }

    @Test
    fun testGetAuthorByUsername() {
        // Given
        val username = "johndoe"

        // When
        val response = getAuthorByUsername(username)

        // Then
        val author = response.returnResult().responseBody
        assertEquals("John", author?.firstName)
        assertEquals("Doe", author?.lastName)
        assertEquals("johndoe", author?.username)
    }

    @Test
    fun testDeleteAuthor() {
        // Given
        val response1 = getAuthorByUsername("johndoe")
        val authorId = response1.returnResult().responseBody?.id

        // When
        deleteAuthorById(authorId!!)

        // Then
        createAuthor(AuthorRequest("John", "Doe", "johndoe"))
    }
}

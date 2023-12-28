package pl.jakubtworek.authors.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.authors.AbstractIT
import pl.jakubtworek.authors.controller.dto.AuthorRequest
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

    @Test
    fun testFollowAuthor() {
        // Given
        val followerId = getAuthorByUsername("johndoe").returnResult().responseBody?.id
        val followingId = getAuthorByUsername("janesmith").returnResult().responseBody?.id

        // When
        followAuthor(followingId!!, "user1-jwt")
        unfollowAuthor(followingId, "user1-jwt")
    }
    // todo: 4 endpointy
    //  jeden zwraca folowersoo konrketnego uzytowniak, (AuthorDTO)
    //  drugi zwraacac followingow danego uzytkownika, (AuthorDTO)
    //  trzeci zwraca wszystkich dla admina (AuthorDTO)
    //  czwarty unfollow
    //  W Authorization rozszerzyc userDetails o liczbe followow i followingow
}

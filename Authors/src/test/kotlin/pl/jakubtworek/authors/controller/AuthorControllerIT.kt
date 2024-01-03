package pl.jakubtworek.authors.controller

import org.junit.jupiter.api.Test
import pl.jakubtworek.authors.AbstractIT
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.common.model.AuthorDTO
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
        val followerId = getAuthorByUsername("johndoe").returnResult().responseBody?.id
        val followingId = getAuthorByUsername("janesmith").returnResult().responseBody?.id

        createAuthor(AuthorRequest("John", "Doe", "johndoe"))
    }

    @Test
    fun testFollowAuthor() {
        // Given
        val followerId = getAuthorByUsername("johndoe").returnResult().responseBody?.id!!
        val followingId = getAuthorByUsername("janesmith").returnResult().responseBody?.id!!

        // When
        followAuthor(followingId, "user1-jwt")

        // Then
        assertAuthorFollowStats(followerId, 0, 1)
        assertAuthorFollowStats(followingId, 1, 0)

        assertAuthorListSizeAndUsername(
            getAuthorsFollowing(followerId, "user1-jwt").returnResult().responseBody,
            1,
            "janesmith"
        )
        assertAuthorListSizeAndUsername(
            getAuthorsFollowers(followerId, "user1-jwt").returnResult().responseBody,
            0,
            null
        )

        assertAuthorListSizeAndUsername(
            getAuthorsFollowing(followingId, "user2-jwt").returnResult().responseBody,
            0,
            null
        )
        assertAuthorListSizeAndUsername(
            getAuthorsFollowers(followingId, "user2-jwt").returnResult().responseBody,
            1,
            "johndoe"
        )

        // Cleanup
        unfollowAuthor(followingId, "user1-jwt")

        // Verify cleanup
        assertAuthorFollowStats(followerId, 0, 0)
        assertAuthorFollowStats(followingId, 0, 0)

        assertAuthorListSizeAndUsername(
            getAuthorsFollowing(followerId, "user1-jwt").returnResult().responseBody,
            0,
            null
        )
        assertAuthorListSizeAndUsername(
            getAuthorsFollowers(followerId, "user1-jwt").returnResult().responseBody,
            0,
            null
        )
        assertAuthorListSizeAndUsername(
            getAuthorsFollowing(followingId, "user2-jwt").returnResult().responseBody,
            0,
            null
        )
        assertAuthorListSizeAndUsername(
            getAuthorsFollowers(followingId, "user2-jwt").returnResult().responseBody,
            0,
            null
        )
    }

    private fun assertAuthorFollowStats(authorId: Int, followers: Int, following: Int) {
        val author = getAuthorById(authorId).returnResult().responseBody
        assertEquals(followers, author?.followers?.size)
        assertEquals(following, author?.following?.size)
    }

    private fun assertAuthorListSizeAndUsername(
        authors: List<AuthorDTO>?,
        expectedSize: Int,
        expectedUsername: String?
    ) {
        assertEquals(expectedSize, authors?.size)
        if (expectedSize > 0) {
            assertEquals(expectedUsername, authors?.get(0)?.username)
        }
    }
}

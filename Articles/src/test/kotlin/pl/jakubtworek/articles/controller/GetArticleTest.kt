package pl.jakubtworek.articles.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pl.jakubtworek.articles.AbstractIT
import pl.jakubtworek.articles.controller.dto.ArticleCreateRequest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetArticleTest : AbstractIT() {
    @Test
    fun shouldReturnLatestArticles() {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getLatestArticles("user-jwt", 0, 6).returnResult().responseBody

        // Then
        assertEquals(6, response?.size)
        assertEquals("Example Article 4", response?.get(0)?.text)
        assertEquals("FirstName", response?.get(0)?.author?.firstName)
        assertEquals("LastName", response?.get(0)?.author?.lastName)
        assertEquals("Username", response?.get(0)?.author?.username)
        assertEquals(0, response?.get(0)?.numOfComments)
        assertEquals(0, response?.get(0)?.likes?.users?.size)
        assertTrue { response?.get(1)?.timestamp!! < response.get(0)?.timestamp!! }

        assertEquals("Example Article 3", response?.get(1)?.text)
        assertEquals("FirstName", response?.get(1)?.author?.firstName)
        assertEquals("LastName", response?.get(1)?.author?.lastName)
        assertEquals("Username", response?.get(1)?.author?.username)
        assertEquals(0, response?.get(1)?.numOfComments)
        assertEquals(0, response?.get(1)?.likes?.users?.size)
        assertTrue { response?.get(2)?.timestamp!! < response.get(1)?.timestamp!! }

        assertEquals("Example Comment 2", response?.get(2)?.text)
        assertEquals("FirstName", response?.get(2)?.author?.firstName)
        assertEquals("LastName", response?.get(2)?.author?.lastName)
        assertEquals("Username", response?.get(2)?.author?.username)
        assertEquals(0, response?.get(2)?.numOfComments)
        assertEquals(0, response?.get(2)?.likes?.users?.size)
        assertTrue { response?.get(3)?.timestamp!! < response.get(2)?.timestamp!! }

        assertEquals("Example Comment 1", response?.get(3)?.text)
        assertEquals("AnotherFirstName", response?.get(3)?.author?.firstName)
        assertEquals("AnotherLastName", response?.get(3)?.author?.lastName)
        assertEquals("AnotherUsername", response?.get(3)?.author?.username)
        assertEquals(0, response?.get(3)?.numOfComments)
        assertEquals(0, response?.get(3)?.likes?.users?.size)
        assertTrue { response?.get(4)?.timestamp!! < response.get(3)?.timestamp!! }

        assertEquals("Example Article 2", response?.get(4)?.text)
        assertEquals("AnotherFirstName", response?.get(4)?.author?.firstName)
        assertEquals("AnotherLastName", response?.get(4)?.author?.lastName)
        assertEquals("AnotherUsername", response?.get(4)?.author?.username)
        assertEquals(1, response?.get(4)?.numOfComments)
        assertEquals(1, response?.get(4)?.likes?.users?.size)
        assertEquals("FirstName LastName", response?.get(4)?.likes?.users?.get(0))
        assertTrue { response?.get(5)?.timestamp!! < response.get(4)?.timestamp!! }

        assertEquals("Example Article 1", response?.get(5)?.text)
        assertEquals("FirstName", response?.get(5)?.author?.firstName)
        assertEquals("LastName", response?.get(5)?.author?.lastName)
        assertEquals("Username", response?.get(5)?.author?.username)
        assertEquals(1, response?.get(5)?.numOfComments)
        assertEquals(2, response?.get(5)?.likes?.users?.size)
        assertEquals("FirstName LastName", response?.get(5)?.likes?.users?.get(0))
        assertEquals("AnotherFirstName AnotherLastName", response?.get(5)?.likes?.users?.get(1))
    }

    @Test
    fun shouldReturnLatestFollowingArticles() {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getLatestFollowingArticles("user-jwt", 0, 6).returnResult().responseBody

        // Then
        assertEquals(2, response?.size)

        assertEquals("Example Comment 1", response?.get(0)?.text)
        assertEquals("AnotherFirstName", response?.get(0)?.author?.firstName)
        assertEquals("AnotherLastName", response?.get(0)?.author?.lastName)
        assertEquals("AnotherUsername", response?.get(0)?.author?.username)
        assertEquals(0, response?.get(0)?.numOfComments)
        assertEquals(0, response?.get(0)?.likes?.users?.size)
        assertTrue { response?.get(1)?.timestamp!! < response.get(0)?.timestamp!! }

        assertEquals("Example Article 2", response?.get(1)?.text)
        assertEquals("AnotherFirstName", response?.get(1)?.author?.firstName)
        assertEquals("AnotherLastName", response?.get(1)?.author?.lastName)
        assertEquals("AnotherUsername", response?.get(1)?.author?.username)
        assertEquals(1, response?.get(1)?.numOfComments)
        assertEquals(1, response?.get(1)?.likes?.users?.size)
        assertEquals("FirstName LastName", response?.get(1)?.likes?.users?.get(0))
    }

    @Test
    fun shouldReturnArticleDetailsById() {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getArticleDetailsById(articleOneID, "user-jwt").returnResult().responseBody

        // Then
        assertEquals("Example Article 1", response?.text)
        assertEquals("FirstName", response?.author?.firstName)
        assertEquals("LastName", response?.author?.lastName)
        assertEquals("Username", response?.author?.username)
        assertEquals(1, response?.comments?.size)
        assertEquals(2, response?.likes?.users?.size)
        assertEquals("FirstName LastName", response?.likes?.users?.get(0))
        assertEquals("AnotherFirstName AnotherLastName", response?.likes?.users?.get(1))
    }

    @Test
    fun shouldReturnArticlesByAuthorId() {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getArticlesByAuthorId(3).returnResult().responseBody

        // Then
        assertEquals(2, response?.size)

        assertEquals("Example Comment 1", response?.get(0)?.text)
        assertEquals(3, response?.get(0)?.authorId)
        assertTrue { response?.get(1)?.timestamp!! < response.get(0)?.timestamp!! }

        assertEquals("Example Article 2", response?.get(1)?.text)
        assertEquals(3, response?.get(0)?.authorId)
    }

    @Test
    fun shouldReturnArticleById() {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getArticleById(articleOneID).returnResult().responseBody

        // Then
        assertEquals("Example Article 1", response?.text)
        assertEquals(2, response?.authorId)
    }

    // paging (parameterized)
    @ParameterizedTest
    @MethodSource("pagingParameters")
    fun shouldPageLatestArticles(expectedSize: Int, page: Int, size: Int) {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getLatestArticles("user-jwt", page, size).returnResult().responseBody

        // Then
        assertEquals(expectedSize, response?.size)
    }

    @ParameterizedTest
    @MethodSource("pagingParametersFollowing")
    fun shouldPageLatestFollowingArticles(expectedSize: Int, page: Int, size: Int) {
        // Given
        val articleOneID =
            saveArticle(ArticleCreateRequest("Example Article 1", null), "user-jwt").returnResult().responseBody?.id
        val articleTwoID =
            saveArticle(
                ArticleCreateRequest("Example Article 2", null),
                "another-user-jwt"
            ).returnResult().responseBody?.id
        saveArticle(ArticleCreateRequest("Example Comment 1", articleOneID), "another-user-jwt")
        saveArticle(ArticleCreateRequest("Example Comment 2", articleTwoID), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 3", null), "user-jwt")
        saveArticle(ArticleCreateRequest("Example Article 4", null), "user-jwt")
        likeArticle(articleTwoID!!, "user-jwt")
        likeArticle(articleOneID!!, "user-jwt")
        likeArticle(articleOneID, "another-user-jwt")

        // When
        val response = getLatestFollowingArticles("user-jwt", page, size).returnResult().responseBody

        // Then
        assertEquals(expectedSize, response?.size)
    }

    companion object {
        @JvmStatic
        fun pagingParameters(): List<Array<Int>> {
            return listOf(
                arrayOf(6, 0, 6),
                arrayOf(2, 0, 2),
                arrayOf(3, 1, 3)
            )
        }

        @JvmStatic
        fun pagingParametersFollowing(): List<Array<Int>> {
            return listOf(
                arrayOf(2, 0, 2),
                arrayOf(1, 0, 1),
                arrayOf(1, 1, 1)
            )
        }
    }
}
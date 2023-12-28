package pl.jakubtworek.authors

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.common.client.ArticleClient
import pl.jakubtworek.common.client.AuthorizationClient
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.UserDetailsDTO

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
abstract class AbstractIT {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @MockBean
    protected lateinit var articleClient: ArticleClient

    @MockBean
    protected lateinit var authorizationClient: AuthorizationClient

    @BeforeEach
    fun setup() {
        val user1 = UserDetailsDTO(1, "John", "Doe", "johndoe", "ROLE_USER")
        val user2 = UserDetailsDTO(2, "Jane", "Smith", "janesmith", "ROLE_USER")
        val admin = UserDetailsDTO(3, "admin", "admin", "admin", "ROLE_ADMIN")
        `when`(articleClient.deleteArticlesByAuthorId(any(Int::class.java)))
            .thenReturn(ResponseEntity.ok(""))
        `when`(authorizationClient.getUserDetails("user1-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(user1)))
        `when`(authorizationClient.getUserDetails("user2-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(user2)))
        `when`(authorizationClient.getUserDetails("admin-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(admin)))

        val authorRequest1 = AuthorRequest("John", "Doe", "johndoe")
        val authorRequest2 = AuthorRequest("Jane", "Smith", "janesmith")

        createAuthor(authorRequest1)
        createAuthor(authorRequest2)
    }

    @AfterEach
    fun clean() {
        val response1 = getAuthorByUsername("johndoe")
        val response2 = getAuthorByUsername("janesmith")

        response1.returnResult().responseBody?.let { deleteAuthorById(it.id) }
        response2.returnResult().responseBody?.let { deleteAuthorById(it.id) }
    }

    fun createAuthor(authorRequest: AuthorRequest) =
        webTestClient.post().uri("/api/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(authorRequest))
            .exchange()
            .expectStatus().isCreated

    fun followAuthor(followingId: Int, jwt: String) =
        webTestClient.put().uri("/api/follow/$followingId")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isCreated

    fun unfollowAuthor(followingId: Int, jwt: String) =
        webTestClient.put().uri("/api/unfollow/$followingId")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isCreated

    fun getAuthors(authorId: Int) =
        webTestClient.get().uri("/api/")
            .header("Authorization", "admin-jwt")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(AuthorDTO::class.java)

    fun getAuthorsFollowing(authorId: Int, jwt: String) =
        webTestClient.get().uri("/api/following")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(AuthorDTO::class.java)

    fun getAuthorsFollowers(authorId: Int, jwt: String) =
        webTestClient.get().uri("/api/followers")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(AuthorDTO::class.java)

    fun getAuthorById(authorId: Int) =
        webTestClient.get().uri("/api/id/$authorId")
            .exchange()
            .expectStatus().isOk
            .expectBody(AuthorDTO::class.java)

    fun getAuthorByUsername(username: String) =
        webTestClient.get().uri("/api/username/$username")
            .exchange()
            .expectStatus().isOk
            .expectBody(AuthorDTO::class.java)

    fun deleteAuthorById(authorId: Int) =
        webTestClient.delete().uri("/api/$authorId")
            .exchange()
            .expectStatus().isOk
            .expectBody().isEmpty
}
package pl.jakubtworek.articles

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.kafka.support.SendResult
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import pl.jakubtworek.articles.controller.dto.*
import pl.jakubtworek.articles.exception.ErrorResponse
import pl.jakubtworek.articles.kafka.message.LikeMessage
import pl.jakubtworek.articles.kafka.service.KafkaLikeService
import pl.jakubtworek.common.client.AuthorClient
import pl.jakubtworek.common.client.AuthorizationClient
import pl.jakubtworek.common.model.ArticleDTO
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.UserDetailsDTO
import java.sql.Timestamp
import java.time.Instant
import java.util.concurrent.CompletableFuture

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
abstract class AbstractIT {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var authorizationClient: AuthorizationClient

    @MockBean
    lateinit var authorClient: AuthorClient

    @MockBean
    lateinit var kafkaLikeService: KafkaLikeService

    @BeforeEach
    fun setup() {
        val admin = UserDetailsDTO(1, "Admin", "Admin", "Admin", "ROLE_ADMIN")
        val user = UserDetailsDTO(2, "FirstName", "LastName", "Username", "ROLE_USER")
        val anotherUser = UserDetailsDTO(3, "AnotherFirstName", "AnotherLastName", "AnotherUsername", "ROLE_USER")
        val author = AuthorDTO(2, "FirstName", "LastName", "Username", mutableListOf(3), mutableListOf())
        val anotherAuthor =
            AuthorDTO(3, "AnotherFirstName", "AnotherLastName", "AnotherUsername", mutableListOf(), mutableListOf(3))
        val likeMessage = LikeMessage(Timestamp.from(Instant.now()), 1, 1)
        Mockito.`when`(authorizationClient.getUserDetails("admin-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(admin)))
        Mockito.`when`(authorizationClient.getUserDetails("user-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(user)))
        Mockito.`when`(authorizationClient.getUserDetails("another-user-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(anotherUser)))
        Mockito.`when`(authorClient.getAuthorById(2))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(author)))
        Mockito.`when`(authorClient.getAuthorById(3))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(anotherAuthor)))
        Mockito.doAnswer {
            CompletableFuture.completedFuture(Mockito.mock<SendResult<String, String>>())
        }.`when`(kafkaLikeService).sendLikeMessage(likeMessage)

        deleteArticles()
    }

    private fun deleteArticles() {
        val articles = getLatestArticles("admin-jwt", 0, 100)
        val articlesId = articles.returnResult().responseBody?.map { it.id }
        articlesId?.forEach { id ->
            deleteArticleById(id, "admin-jwt")
        }
    }

    // POST
    fun saveArticle(articleRequest: ArticleCreateRequest, jwt: String) =
        webTestClient.post().uri("/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .bodyValue(articleRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(ArticleResponse::class.java)

    fun saveArticleAndReturnError(articleRequest: ArticleCreateRequest, jwt: String) =
        webTestClient.post().uri("/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .bodyValue(articleRequest)
            .exchange()
            .expectBody(ErrorResponse::class.java)

    fun likeArticle(articleId: Int, jwt: String) =
        webTestClient.post().uri("/articles/like/$articleId")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isCreated
            .expectBody(LikeActionResponse::class.java)

    fun likeArticleAndReturnError(articleId: Int, jwt: String) =
        webTestClient.post().uri("/articles/like/$articleId")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .exchange()
            .expectBody(ErrorResponse::class.java)

    // PUT
    fun updateArticle(articleRequest: ArticleUpdateRequest, jwt: String) =
        webTestClient.put().uri("/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .bodyValue(articleRequest)
            .exchange()
            .expectStatus().isOk

    fun updateArticleAndReturnError(articleRequest: ArticleUpdateRequest, jwt: String) =
        webTestClient.put().uri("/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jwt)
            .bodyValue(articleRequest)
            .exchange()
            .expectBody(ErrorResponse::class.java)

    // GET
    fun getLatestArticles(jwt: String, page: Int, size: Int) =
        webTestClient.get().uri("/articles?page=$page&size=$size")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleResponse::class.java)

    fun getLatestFollowingArticles(jwt: String, page: Int, size: Int) =
        webTestClient.get().uri("/articles/authors/followed?page=$page&size=$size")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleResponse::class.java)

    fun getArticleDetailsById(articleId: Int, jwt: String) =
        webTestClient.get().uri("/articles/$articleId")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk
            .expectBody(ArticleDetailsResponse::class.java)

    fun getArticleDetailsAndReturnError(articleId: Int, jwt: String) =
        webTestClient.get().uri("/articles/$articleId")
            .header("Authorization", jwt)
            .exchange()
            .expectBody(ErrorResponse::class.java)

    fun getArticlesByAuthorId(authorId: Int) =
        webTestClient.get().uri("/articles/author/$authorId")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleDTO::class.java)

    fun getArticleById(articleId: Int) =
        webTestClient.get().uri("/articles/id/$articleId")
            .exchange()
            .expectStatus().isOk
            .expectBody(ArticleDTO::class.java)

    // DELETE
    fun deleteArticleById(articleId: Int, jwt: String) =
        webTestClient.delete().uri("/articles/$articleId")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isNoContent
            .expectBody().isEmpty

    fun deleteArticleByIdAndReturnError(articleId: Int, jwt: String) =
        webTestClient.delete().uri("/articles/$articleId")
            .header("Authorization", jwt)
            .exchange()
            .expectBody(ErrorResponse::class.java)

    fun deleteArticlesByAuthorId(authorId: Int) =
        webTestClient.delete().uri("/articles/authorId/$authorId")
            .exchange()
            .expectStatus().isNoContent
            .expectBody().isEmpty
}

package pl.jakubtworek.articles

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.kafka.support.SendResult
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse
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
    protected lateinit var webTestClient: WebTestClient

    @MockBean
    protected lateinit var authorizationClient: AuthorizationClient

    @MockBean
    protected lateinit var authorClient: AuthorClient

    @MockBean
    protected lateinit var kafkaLikeService: KafkaLikeService

    @BeforeEach
    fun setup() {
        val admin = UserDetailsDTO(1, "FirstName", "LastName", "Username", "ROLE_ADMIN")
        val user = UserDetailsDTO(1, "FirstName", "LastName", "Username", "ROLE_USER")
        val author = AuthorDTO(1, "FirstName", "LastName", "Username", mutableListOf(), mutableListOf())
        val likeMessage = LikeMessage(Timestamp.from(Instant.now()), 1, 1)
        Mockito.`when`(authorizationClient.getUserDetails("admin-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(admin)))
        Mockito.`when`(authorizationClient.getUserDetails("user-jwt"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(user)))
        Mockito.`when`(authorClient.getAuthorById(1))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(author)))
        Mockito.doAnswer {
            CompletableFuture.completedFuture(Mockito.mock<SendResult<String, String>>())
        }.`when`(kafkaLikeService).sendLikeMessage(likeMessage)

        val articles = webTestClient.get().uri("/api/?page=0&size=50")
            .header("Authorization", "admin-jwt")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleResponse::class.java)

        val articlesId = articles.returnResult().responseBody?.map { it.id }

        articlesId?.forEach { id ->
            deleteArticleByArticleId(id)
        }


        val headers = HttpHeaders()
        headers.set("Authorization", "dummy-jwt")

        val articleRequest1 = ArticleRequest("Example Content 1")
        val articleRequest2 = ArticleRequest("Example Content 2")

        createArticle(articleRequest1)
        createArticle(articleRequest2)
    }

    @AfterEach
    fun clean() {
        val articles = webTestClient.get().uri("/api/?page=0&size=50")
            .header("Authorization", "admin-jwt")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleResponse::class.java)

        val articlesId = articles.returnResult().responseBody?.map { it.id }

        articlesId?.forEach { id ->
            deleteArticleByArticleId(id)
        }
    }

    fun createArticle(articleRequest: ArticleRequest) =
        webTestClient.post().uri("/api/")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "user-jwt")
            .bodyValue(articleRequest)
            .exchange()
            .expectStatus().isCreated

    fun updateArticle(articleRequest: ArticleRequest, articleId: Int) =
        webTestClient.put().uri("/api/$articleId")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "user-jwt")
            .bodyValue(articleRequest)
            .exchange()
            .expectStatus().isOk

    fun getArticles(expectedSize: Int) =
        webTestClient.get().uri("/api/")
            .header("Authorization", "user-jwt")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleResponse::class.java)
            .hasSize(expectedSize)

    fun getArticleById(articleId: Int) =
        webTestClient.get().uri("/api/id/$articleId")
            .exchange()
            .expectStatus().isOk
            .expectBody(ArticleDTO::class.java)

    fun getArticlesByAuthorId(authorId: Int, expectedSize: Int) =
        webTestClient.get().uri("/api/author/$authorId")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ArticleDTO::class.java)
            .hasSize(expectedSize)

    fun deleteArticleByArticleId(articleId: Int) =
        webTestClient.delete().uri("/api/$articleId")
            .header("Authorization", "user-jwt")
            .exchange()
            .expectStatus().isNoContent
            .expectBody().isEmpty

    fun deleteArticleByAuthorId(authorId: Int) =
        webTestClient.delete().uri("/api/authorId/$authorId")
            .exchange()
            .expectStatus().isNoContent
            .expectBody().isEmpty

    fun likeArticle(articleId: Int) =
        webTestClient.post().uri("/api/like/$articleId")
            .header("Authorization", "user-jwt")
            .exchange()
            .expectStatus().isCreated
            .expectBody(LikeResponse::class.java)
}
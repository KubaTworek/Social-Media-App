package pl.jakubtworek.authors

import pl.jakubtworek.authors.controller.dto.AuthorRequest
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
import pl.jakubtworek.common.client.ArticleClient
import pl.jakubtworek.common.model.AuthorDTO

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
abstract class AbstractIT {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @MockBean
    protected lateinit var articleClient: ArticleClient

    @BeforeEach
    fun setup() {
        `when`(articleClient.deleteArticlesByAuthorId(any(Int::class.java)))
            .thenReturn(ResponseEntity.ok(""))


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
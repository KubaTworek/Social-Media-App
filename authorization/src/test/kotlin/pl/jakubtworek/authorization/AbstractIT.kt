package pl.jakubtworek.authorization

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.common.client.AuthorClient
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.AuthorRequest
import pl.jakubtworek.common.model.UserDetailsDTO

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
abstract class AbstractIT {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @MockBean
    protected lateinit var authorClient: AuthorClient

    @BeforeEach
    fun setup() {
        val authorRequest = AuthorRequest("John", "Doe", "username")
        val author = AuthorDTO(1, "John", "Doe", "username", mutableListOf(), mutableListOf())
        `when`(authorClient.deleteAuthorById(1))
            .thenReturn(ResponseEntity.ok().build())
        `when`(authorClient.createAuthor(authorRequest))
            .thenReturn(ResponseEntity.ok().build())
        `when`(authorClient.getAuthorByUsername("username"))
            .thenReturn(ResponseEntity.ok(ObjectMapper().writeValueAsString(author)))
    }

    @AfterEach
    fun clean() {
        val loginRequest = LoginRequest("username", "password")
        val loginResponse = loginUser(loginRequest)
            .expectStatus().isAccepted
            .expectBody(LoginResponse::class.java)
        val jwt = loginResponse
            .returnResult().responseBody?.token
        deleteUser(jwt!!)
    }

    fun registerUser(registerRequest: RegisterRequest) =
        webTestClient.post().uri("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(registerRequest))
            .exchange()

    fun deleteUser(jwt: String) =
        webTestClient.delete().uri("/api/delete")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk

    fun loginUser(loginRequest: LoginRequest) =
        webTestClient.post().uri("/api/login")
            .body(BodyInserters.fromValue(loginRequest))
            .exchange()

    fun getUserDetailsAfterLogin(jwt: String) =
        webTestClient.get().uri("/api/user-info")
            .header("Authorization", jwt)
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDetailsDTO::class.java)
}
package pl.jakubtworek.notifications

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import pl.jakubtworek.common.client.ArticleClient
import pl.jakubtworek.common.client.AuthorClient
import pl.jakubtworek.common.client.AuthorizationClient
import pl.jakubtworek.notifications.controller.dto.NotificationResponse

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
    protected lateinit var articleClient: ArticleClient

    fun getNotifications(expectedSize: Int) =
        webTestClient.get().uri("/api/")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(NotificationResponse::class.java)
            .hasSize(expectedSize)

    fun updateNotifications(notifiactionId: Int, authorId: Int) =
        webTestClient.get().uri("/api/${notifiactionId}/author/${authorId}")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Unit::class.java)
}
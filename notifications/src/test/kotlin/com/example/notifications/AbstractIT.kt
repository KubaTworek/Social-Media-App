package com.example.notifications

import com.example.notifications.client.*
import com.example.notifications.controller.dto.NotificationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

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
}
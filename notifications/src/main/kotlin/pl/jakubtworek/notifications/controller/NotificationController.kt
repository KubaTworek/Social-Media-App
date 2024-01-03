package pl.jakubtworek.notifications.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.common.Constants.AUTHORIZATION_HEADER
import pl.jakubtworek.notifications.controller.dto.AuthorWithActivityResponse
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.service.NotificationService

@RequestMapping("/api")
@RestController
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/admin", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllNotificationsForAdmin(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String
    ): ResponseEntity<List<NotificationResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(notificationService.getAllNotificationsForAdmin(jwt))

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllNotificationsByUser(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String
    ): ResponseEntity<List<NotificationResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(notificationService.getAllNotificationsByUser(jwt))

    @GetMapping("/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuthorActivities(
        @PathVariable authorId: Int
    ): ResponseEntity<AuthorWithActivityResponse> = ResponseEntity.status(HttpStatus.OK)
        .body(notificationService.getAuthorActivities(authorId))

    @PutMapping("/{notificationId}/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateNotificationAuthor(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @PathVariable notificationId: Int,
        @PathVariable authorId: Int
    ) = notificationService.updateNotificationAuthor(jwt, notificationId, authorId)
}

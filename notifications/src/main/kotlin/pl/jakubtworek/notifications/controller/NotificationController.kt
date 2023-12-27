package pl.jakubtworek.notifications.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.service.NotificationService

@RequestMapping("/api")
@RestController
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/admin", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllNotifications(
        @RequestHeader("Authorization") jwt: String
    ): ResponseEntity<List<NotificationResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(notificationService.findAllNotifications(jwt))

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllNotificationsByUser(
        @RequestHeader("Authorization") jwt: String
    ): ResponseEntity<List<NotificationResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(notificationService.findAllNotificationsByUser(jwt))

    @PutMapping("/{notificationId}/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAuthorNotification(
        @RequestHeader("Authorization") jwt: String,
        @PathVariable notificationId: Int,
        @PathVariable authorId: Int
    ) = notificationService.update(jwt, notificationId, authorId)
}

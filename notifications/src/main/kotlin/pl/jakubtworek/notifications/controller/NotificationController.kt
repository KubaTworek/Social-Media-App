package pl.jakubtworek.notifications.controller

import org.springframework.web.bind.annotation.*
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.service.NotificationService

@RequestMapping("/api")
@RestController
class NotificationController(private val notificationService: NotificationService) {
    // EXTERNAL

    @GetMapping("/admin")
    fun getAllNotifications(
        @RequestHeader("Authorization") jwt: String
    ): List<NotificationResponse> =
        notificationService.findAllNotifications(jwt)

    @GetMapping("/")
    fun getAllNotificationsByUser(
        @RequestHeader("Authorization") jwt: String
    ): List<NotificationResponse> =
        notificationService.findAllNotificationsByUser(jwt)

    @PutMapping("/{notificationId}/author/{authorId}")
    fun updateAuthorNotification(
        @RequestHeader("Authorization") jwt: String,
        @PathVariable notificationId: Int,
        @PathVariable authorId: Int
    ): Unit =
        notificationService.update(jwt, notificationId, authorId)
}
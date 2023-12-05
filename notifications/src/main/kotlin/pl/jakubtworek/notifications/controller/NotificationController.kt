package pl.jakubtworek.notifications.controller

import org.springframework.web.bind.annotation.*
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.service.NotificationService

@RequestMapping("/api")
@RestController
class NotificationController(private val notificationService: NotificationService) {
    // EXTERNAL
    @GetMapping("/")
    fun getAllNotifications(
        @RequestHeader("Authorization") jwt: String
    ): List<NotificationResponse> =
        notificationService.findAllNotificationsByUser(jwt)
}
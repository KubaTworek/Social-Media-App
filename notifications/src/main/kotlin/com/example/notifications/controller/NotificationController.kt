package com.example.notifications.controller

import com.example.notifications.controller.dto.NotificationResponse
import com.example.notifications.service.NotificationService
import org.springframework.web.bind.annotation.*

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
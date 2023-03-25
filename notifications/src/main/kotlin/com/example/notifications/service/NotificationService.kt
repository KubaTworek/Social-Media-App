package com.example.notifications.service

import com.example.notifications.controller.dto.NotificationResponse

interface NotificationService {
    fun findAllNotificationsByUser(jwt: String): List<NotificationResponse>
    fun processLikeMessage(message: String)
}
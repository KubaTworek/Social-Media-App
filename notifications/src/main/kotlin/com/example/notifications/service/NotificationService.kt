package com.example.notifications.service

import com.example.notifications.model.entity.Notification

interface NotificationService {
    fun findAllNotificationsByUser(jwt: String): List<Notification>
    fun getLike(message: String)
}
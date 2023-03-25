package com.example.notifications.controller.dto

import java.sql.Timestamp

data class NotificationResponse(
    val message: String,
    val content: String,
    val timestamp: Timestamp,
)

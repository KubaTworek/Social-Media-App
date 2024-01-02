package pl.jakubtworek.notifications.model.message

import java.sql.Timestamp

data class FollowMessage(
    val timestamp: Timestamp,
    val followerId: Int,
    val followedId: Int
)
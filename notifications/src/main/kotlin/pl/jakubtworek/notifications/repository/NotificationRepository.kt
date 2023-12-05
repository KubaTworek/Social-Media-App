package pl.jakubtworek.notifications.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.jakubtworek.notifications.model.entity.Notification
import java.util.*

interface NotificationRepository : JpaRepository<Notification, Int> {
    fun findAllByArticleIdOrderByTimestampDesc(articleId: Int): List<Notification>
    fun findByArticleIdAndAuthorId(articleId: Int, authorId: Int): Optional<Notification>
}
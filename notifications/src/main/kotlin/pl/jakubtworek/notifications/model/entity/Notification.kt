package pl.jakubtworek.notifications.model.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "NOTIFICATIONS")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTIFICATION_ID")
    val id: Int,

    @Column(name = "ARTICLE_ID")
    val articleId: Int,

    @Column(name = "AUTHOR_ID")
    val authorId: Int,

    @Column(name = "CREATE_AT")
    val createAt: Timestamp,

    @Column(name = "TYPE")
    val type: String
) {
    constructor() : this(
        0,
        0,
        0,
        Timestamp(System.currentTimeMillis()),
        ""
    )

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}
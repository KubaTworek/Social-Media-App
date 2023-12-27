package pl.jakubtworek.notifications.model.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
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
    var authorId: Int,

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
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Notification

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "Notification(id=$id, articleId=$articleId, authorId=$authorId, createAt=$createAt, type='$type')"
    }
}

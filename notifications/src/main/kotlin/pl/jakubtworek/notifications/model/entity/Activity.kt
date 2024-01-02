package pl.jakubtworek.notifications.model.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.sql.Timestamp

@Entity
@Table(name = "ACTIVITIES")
data class Activity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACTIVITY_ID")
    val id: Int,

    @Column(name = "TARGET_ID")
    val targetId: Int,

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
        other as Activity

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "Notification(id=$id, articleId=$targetId, authorId=$authorId, createAt=$createAt, type='$type')"
    }
}

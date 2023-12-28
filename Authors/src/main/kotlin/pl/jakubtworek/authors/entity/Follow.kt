package pl.jakubtworek.authors.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.sql.Timestamp

@Entity
@Table(name = "FOLLOW")
data class Follow(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FOLLOW_ID")
    val id: Int,

    @ManyToOne
    @JoinColumn(name = "FOLLOWER_ID")
    val follower: Author,

    @ManyToOne
    @JoinColumn(name = "FOLLOWING_ID")
    val following: Author,

    @Column(name = "CREATE_AT")
    val createAt: Timestamp,
) {
    constructor() : this(
        0,
        Author(),
        Author(),
        Timestamp(System.currentTimeMillis())
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Follow

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "Follow(id=$id, follower=${follower.username}, following=${following.username}, createAt=$createAt)"
    }
}

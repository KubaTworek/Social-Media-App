package pl.jakubtworek.articles.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "LIKES")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LIKE_ID")
    val id: Int,

    @Column(name = "CREATE_AT")
    val createAt: Timestamp,

    @Column(name = "AUTHOR_ID")
    val authorId: Int,

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    val article: Article
) {
    constructor() : this(
        0,
        Timestamp(System.currentTimeMillis()),
        0,
        Article()
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
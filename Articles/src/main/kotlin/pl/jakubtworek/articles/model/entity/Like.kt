package pl.jakubtworek.articles.model.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "Like_Article")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    val id: Int,

    @Column(name = "Timestamp")
    val timestamp: Timestamp,

    @Column(name = "Author_Id")
    val authorId: Int,

    @Column(name = "Article_Id")
    val articleId: Int,
) {
    constructor() : this(
        0,
        Timestamp(System.currentTimeMillis()),
        0,
        0
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
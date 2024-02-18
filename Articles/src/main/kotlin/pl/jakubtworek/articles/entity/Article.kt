package pl.jakubtworek.articles.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.sql.Timestamp

@Entity
@Table(name = "ARTICLES")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ARTICLE_ID")
    val id: Int,

    @Column(name = "CREATE_AT")
    val createAt: Timestamp,

    @Column(name = "CONTENT")
    val content: String,

    @Column(name = "AUTHOR_ID")
    val authorId: Int,

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<Like> = mutableListOf(),

    @OneToMany(mappedBy = "motherArticle", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val articles: MutableList<Article> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOTHER_ARTICLE_ID")
    val motherArticle: Article? = null,
) {
    constructor() : this(
        0,
        Timestamp(System.currentTimeMillis()),
        "",
        0
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Article

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "Article(id=$id, timestamp=$createAt, text=$content, authorId=$authorId, motherArticleId=${motherArticle?.id})"
    }
}

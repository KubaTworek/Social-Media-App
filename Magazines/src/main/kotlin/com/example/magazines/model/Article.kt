package com.example.magazines.model

import jakarta.persistence.*
import org.hibernate.Hibernate


@Entity
@Table(name = "ArticlePost")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    val id: Int,

    @Column(name = "Date")
    val date: String,

    @Column(name = "Timestamp")
    val timestamp: String,

    @Column(name = "Author_Id")
    var authorId: Int,

    @Column(name = "Magazine_Id")
    var magazineId: Int,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "Content_Id")
    val content: ArticleContent,

    ) {
    constructor() : this(
        0,
        "",
        "",
        0,
        0,
        ArticleContent()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Article

        return id == other.id
    }

    override fun hashCode(): Int = hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , date = $date , timestamp = $timestamp , authorId = $authorId , magazineId = $magazineId , content = $content )"
    }
}
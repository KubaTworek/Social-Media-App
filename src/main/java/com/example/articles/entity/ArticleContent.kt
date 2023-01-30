package com.example.articles.entity

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "Content")
data class ArticleContent(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int,

    @Column(name = "title")
    val title: String,

    @Column(name = "text")
    val text: String,

    @OneToOne(mappedBy = "content")
    val article: ArticlePost?,
) {
    constructor() : this(
        0,
        "asd",
        "asd",
        null
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ArticleContent

        return id == other.id
    }

    override fun hashCode(): Int = hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , text = $text , article = $article )"
    }
}
package com.example.articles.model.entity

import com.example.articles.model.dto.ArticleDTO
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.sql.Timestamp


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
    val timestamp: Timestamp,

    @Column(name = "Text")
    val text: String,

    @Column(name = "Author_Id")
    val authorId: Int,

    ) {
    constructor() : this(
        0,
        "",
        Timestamp(System.currentTimeMillis()),
        "",
        0
    )

    fun toDTO(): ArticleDTO = ArticleDTO(
        this.id,
        this.date,
        this.timestamp.toString(),
        this.text,
        this.authorId,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Article

        return id == other.id
    }

    override fun hashCode(): Int = hashCode()

    override fun toString(): String {
        return super.toString()
    }
}
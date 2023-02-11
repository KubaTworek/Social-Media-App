package com.example.articles.model.entity

import com.example.articles.model.dto.ArticleDTO
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
    val authorId: Int,

    @Column(name = "Magazine_Id")
    val magazineId: Int,

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

    fun toDTO(): ArticleDTO = ArticleDTO(
        this.id,
        this.date,
        this.timestamp,
        this.authorId,
        this.magazineId,
        this.content.toDTO()
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
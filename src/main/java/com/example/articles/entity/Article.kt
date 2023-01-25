package com.example.articles.entity

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "Article")
data class Article (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    val id: Int,

    @Column(name = "Date")
    val date: String,

    @Column(name = "Timestamp")
    val timestamp: Long,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "Author_Id")
    var author: Author,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "Magazine_Id")
    var magazine: Magazine,

    @OneToOne(cascade = [CascadeType.ALL, CascadeType.ALL])
    @JoinColumn(name = "Content_Id")
    val content: ArticleContent,


    ) {
    constructor() : this(
        0,
        "sad",
        123412,
        Author(),
        Magazine(),
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
        return this::class.simpleName + "(id = $id , date = $date , timestamp = $timestamp , author = $author , magazine = $magazine , content = $content )"
    }
}
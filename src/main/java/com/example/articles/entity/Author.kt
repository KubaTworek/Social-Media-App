package com.example.articles.entity

import com.example.articles.controller.author.AuthorResponse
import org.hibernate.Hibernate
import java.util.Collections.emptyList
import javax.persistence.*
import kotlin.streams.toList

@Entity
@Table(name = "Author")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int,

    @Column(name = "firstname")
    val firstName: String,

    @Column(name = "lastname")
    val lastName: String,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    val articles: MutableList<Article>
) {
    constructor() : this(
        0,
        "asd",
        "asd",
        emptyList<Article>()
    )

    fun toResponse(): AuthorResponse {
        return AuthorResponse(
            this.id,
            this.firstName,
            this.lastName,
            this.articles.stream().map { it.toResponse() }.toList()
        )
    }

    fun add(tempArticle: Article) {
        articles.add(tempArticle)
        tempArticle.author = this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Author

        return id == other.id
    }

    override fun hashCode(): Int = hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , firstName = $firstName , lastName = $lastName )"
    }
}
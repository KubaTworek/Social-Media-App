package com.example.articles.entity

import com.example.articles.controller.MagazineResponse
import org.hibernate.Hibernate
import java.util.Collections.emptyList
import javax.persistence.*
import kotlin.streams.toList

@Entity
@Table(name = "Magazine")
data class Magazine(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    val id: Int,

    @Column(name = "Name")
    val name: String,

    @OneToMany(mappedBy = "magazine", cascade = [CascadeType.ALL])
    val articles: MutableList<Article>
) {
    constructor() : this(
        0,
        "asd",
        emptyList<Article>()
    )

    fun toResponse(): MagazineResponse {
        return MagazineResponse(
            this.id,
            this.name,
            this.articles.stream().map { it.toResponse() }.toList()
        )
    }


    fun add(tempArticle: Article) {
        articles.add(tempArticle)
        tempArticle.magazine = this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Magazine

        return id == other.id
    }

    override fun hashCode(): Int = hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name )"
    }
}
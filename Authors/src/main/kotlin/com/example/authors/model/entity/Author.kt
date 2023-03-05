package com.example.authors.model.entity

import com.example.authors.model.dto.AuthorDTO
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "AuthorPost")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int,

    @Column(name = "firstname")
    val firstName: String,

    @Column(name = "lastname")
    val lastName: String,

    @Column(name = "username")
    val username: String,
) {
    constructor() : this(
        0,
        "",
        "",
        ""
    )

    fun toDTO() = AuthorDTO(
        this.id,
        this.firstName,
        this.lastName
    )

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
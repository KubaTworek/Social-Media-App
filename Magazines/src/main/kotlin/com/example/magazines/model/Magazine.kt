package com.example.magazines.model

import com.example.magazines.model.dto.MagazineDTO
import jakarta.persistence.*
import org.hibernate.Hibernate


@Entity
@Table(name = "MagazinePost")
data class Magazine(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    val id: Int,

    @Column(name = "Name")
    val name: String,

    ) {
    constructor() : this(
        0,
        ""
    )

    fun toDTO() = MagazineDTO(
        this.id,
        this.name
    )

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
package pl.jakubtworek.authors.entity

import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "AUTHORS")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUTHOR_ID")
    val id: Int,

    @Column(name = "FIRST_NAME")
    val firstName: String,

    @Column(name = "LAST_NAME")
    val lastName: String,

    @Column(name = "USERNAME")
    val username: String,
) {
    constructor() : this(
        0,
        "",
        "",
        ""
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

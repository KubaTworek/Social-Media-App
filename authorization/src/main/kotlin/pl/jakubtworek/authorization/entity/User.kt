package pl.jakubtworek.authorization.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "USERS")
data class User(
    @Id
    @Column(name = "USERNAME")
    val username: String,

    @Column(name = "PASSWORD")
    val password: String,

    @Column(name = "ROLE")
    val role: String
) {
    constructor() : this(
        "username",
        "password",
        "ROLE_USER"
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return username == other.username
    }

    override fun hashCode(): Int = username.hashCode()

    override fun toString(): String {
        return "User(username=$username, password=$password, role=$role)"
    }
}

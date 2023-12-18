package pl.jakubtworek.authorization.entity

import jakarta.persistence.*
import lombok.Builder

@Builder
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
}

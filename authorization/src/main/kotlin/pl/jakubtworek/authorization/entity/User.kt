package pl.jakubtworek.authorization.entity

import jakarta.persistence.*
import lombok.Builder

@Builder
@Entity
@Table(name = "userX")
data class User(
    @Id
    @Column(name = "username")
    val username: String,

    @Column(name = "password")
    val password: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    val authorities: Authorities
) {
    constructor() : this(
        "username",
        "password",
        Authorities()
    )
}

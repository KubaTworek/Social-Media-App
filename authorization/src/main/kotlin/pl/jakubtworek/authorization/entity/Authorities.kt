package pl.jakubtworek.authorization.entity

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "authorities")
data class Authorities(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int,

    @get: NotNull
    var authority: String,

    @OneToMany(mappedBy = "authorities", cascade = [CascadeType.ALL], orphanRemoval = true)
    val users: MutableList<User>
) {
    constructor() : this(
        0,
        "authority",
        mutableListOf()
    )
}

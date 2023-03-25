package com.example.notifications.model.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "Notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Int,

    @Column(name = "Article_Id")
    val articleId: Int,

    @Column(name = "Author_Id")
    val authorId: Int,

    @Column(name = "Timestamp")
    val timestamp: Timestamp,

    @Column(name = "Type")
    val type: String
) {
    constructor() : this(
        0,
        0,
        0,
        Timestamp(System.currentTimeMillis()),
        ""
    )

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}
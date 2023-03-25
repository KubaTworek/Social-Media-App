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

    @Column(name = "Text")
    val text: String,

    @Column(name = "Timestamp")
    val timestamp: Timestamp
) {
    constructor() : this(
        0,
        "",
        Timestamp(System.currentTimeMillis())
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
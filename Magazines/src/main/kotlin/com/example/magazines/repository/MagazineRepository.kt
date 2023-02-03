package com.example.magazines.repository

import com.example.magazines.model.Magazine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MagazineRepository : JpaRepository<Magazine, Int> {
    fun findByName(name: String): Optional<Magazine>
}
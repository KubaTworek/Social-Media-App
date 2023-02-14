package com.example.magazines.repository

import com.example.magazines.model.entity.Magazine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MagazineRepository : JpaRepository<Magazine, Int> {
    fun findAllByNameContaining(keyword: String): List<Magazine>

}
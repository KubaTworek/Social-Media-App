package com.example.articles.repository

import com.example.articles.entity.MagazinePost
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MagazineRepository : JpaRepository<MagazinePost, Int> {
    fun findByName(name: String): Optional<MagazinePost>
}
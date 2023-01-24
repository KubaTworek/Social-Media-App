package com.example.RESTAPIarticle.repository

import com.example.RESTAPIarticle.entity.Magazine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MagazineRepository : JpaRepository<Magazine, Int> {
    fun findByName(name: String): Magazine
}
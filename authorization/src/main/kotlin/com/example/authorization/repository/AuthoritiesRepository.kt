package com.example.authorization.repository

import com.example.authorization.entity.Authorities
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthoritiesRepository : JpaRepository<Authorities, Int> {
    fun findAuthoritiesByAuthority(authority: String): Optional<Authorities>
}

package pl.jakubtworek.authorization.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.jakubtworek.authorization.entity.Authorities

@Repository
interface AuthoritiesRepository : JpaRepository<Authorities, Int> {
    fun findAuthoritiesByAuthority(authority: String): Authorities?
}

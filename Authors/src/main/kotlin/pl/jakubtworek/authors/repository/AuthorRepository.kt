package pl.jakubtworek.authors.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.jakubtworek.authors.entity.Author
import java.util.*


@Repository
interface AuthorRepository : JpaRepository<Author, Int> {
    fun findAuthorByUsername(username: String): Optional<Author>
}

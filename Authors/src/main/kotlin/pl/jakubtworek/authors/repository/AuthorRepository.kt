package pl.jakubtworek.authors.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.jakubtworek.authors.entity.Author
import java.util.*


@Repository
interface AuthorRepository : JpaRepository<Author, Int> {

    fun findAuthorByUsername(username: String): Optional<Author>

    @Query(
        "SELECT a FROM Author a " +
                "LEFT JOIN FETCH a.followedBy f " +
                "WHERE f.follower.id = :theId"
    )
    fun findAllFollowing(@Param("theId") theId: Int): List<Author>

    @Query(
        "SELECT a FROM Author a " +
                "LEFT JOIN FETCH a.following f " +
                "WHERE f.following.id = :theId"
    )
    fun findAllFollowers(@Param("theId") theId: Int): List<Author>
}

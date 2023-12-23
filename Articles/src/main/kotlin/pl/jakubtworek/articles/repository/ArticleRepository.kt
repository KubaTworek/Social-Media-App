package pl.jakubtworek.articles.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.jakubtworek.articles.entity.Article
import java.util.*

@Repository
interface ArticleRepository : JpaRepository<Article, Int> {

    @Query(
        "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.likes"
    )
    override fun findAll(): List<Article>

    @Query(
        "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.likes " +
                "WHERE a.id = :articleId"
    )
    override fun findById(@Param("articleId") articleId: Int): Optional<Article>

    fun findAllByAuthorIdOrderByCreateAt(authorId: Int): List<Article>

    @Transactional
    fun deleteAllByAuthorId(authorId: Int)
}

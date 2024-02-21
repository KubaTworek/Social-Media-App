package pl.jakubtworek.articles.repository

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
                "LEFT JOIN FETCH a.likes",
        countQuery = "SELECT COUNT(a) FROM Article a"
    )
    override fun findAll(pageable: Pageable): Page<Article>

    @Query(
        "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.likes"
    )
    override fun findAll(): List<Article>

    fun countAllByMotherArticleId(motherArticleId: Int): Int

    @Query(
        "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.likes " +
                "WHERE a.id = :articleId"
    )
    override fun findById(@Param("articleId") articleId: Int): Optional<Article>

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.likes WHERE a.id = :articleId")
    fun findByIdWithLikes(@Param("articleId") articleId: Int): Optional<Article>

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.articles WHERE a.id = :articleId")
    fun findByIdWithArticles(@Param("articleId") articleId: Int): Optional<Article>

    @Query(
        "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.likes " +
                "WHERE a.authorId = :authorId " +
                "ORDER BY a.createAt DESC"
    )
    fun findAllByAuthorIdOrderByCreateAt(@Param("authorId") authorId: Int): List<Article>

    @Query(
        "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.likes " +
                "WHERE a.authorId IN :authorIds " +
                "ORDER BY a.createAt DESC"
    )
    fun findAllByAuthorIdInOrderByCreateAt(@Param("authorIds") authorIds: List<Int>, pageable: Pageable): Page<Article>

    @Transactional
    fun deleteAllByAuthorId(authorId: Int)
}

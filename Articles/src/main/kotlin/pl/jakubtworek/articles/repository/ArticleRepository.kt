package pl.jakubtworek.articles.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.jakubtworek.articles.model.entity.Article

@Repository
interface ArticleRepository : JpaRepository<Article, Int> {
    fun findAllByAuthorIdOrderByDate(authorId: Int): List<Article>

    @Transactional
    fun deleteAllByAuthorId(authorId: Int)
}

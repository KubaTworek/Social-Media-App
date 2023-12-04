package pl.jakubtworek.articles.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.jakubtworek.articles.model.entity.Like

@Repository
interface LikeRepository : JpaRepository<Like, Int> {
    fun countLikesByArticleId(articleId: Int): Int
    fun findByArticleId(articleId: Int): List<Like>
    fun findByArticleIdAndAuthorId(articleId: Int, authorId: Int): Like?
    fun deleteAllByArticleId(articleId: Int)
}

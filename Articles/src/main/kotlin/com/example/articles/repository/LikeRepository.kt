package com.example.articles.repository


import com.example.articles.model.entity.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository : JpaRepository<Like, Int> {
    fun countLikesByArticleId(articleId: Int): Int
    fun findByArticleIdAndAuthorId(articleId: Int, authorId: Int): Like?
}

package com.example.articles.repository

import com.example.articles.model.entity.Article
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Int> {
    fun findAllByAuthorIdOrderByDate(authorId: Int): List<Article>
    @Transactional
    fun deleteAllByAuthorId(authorId: Int)
}

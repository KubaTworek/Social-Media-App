package com.example.articles.repository

import com.example.articles.model.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Int> {
    fun findAllByAuthorIdOrderByDate(authorId: Int): List<Article>
    fun findAllByMagazineIdOrderByDate(magazineId: Int): List<Article>
}
package com.example.RESTAPIarticle.repository

import com.example.RESTAPIarticle.entity.ArticleContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : JpaRepository<ArticleContent?, Int?> {
    fun findAllByTextContainsOrTitleContains(keyword1: String?, keyword2: String?): List<ArticleContent?>?
}
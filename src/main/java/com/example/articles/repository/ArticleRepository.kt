package com.example.articles.repository

import com.example.articles.entity.ArticlePost
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<ArticlePost, Int>
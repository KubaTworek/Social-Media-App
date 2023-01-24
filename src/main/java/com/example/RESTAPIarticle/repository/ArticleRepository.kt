package com.example.RESTAPIarticle.repository

import com.example.RESTAPIarticle.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Int>
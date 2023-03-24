package com.example.articles.service

interface LikeService {
    fun like(articleId: Int, jwt: String)
}
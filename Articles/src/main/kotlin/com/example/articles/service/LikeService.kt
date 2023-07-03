package com.example.articles.service

import com.example.articles.controller.dto.LikeResponse

interface LikeService {
    fun like(articleId: Int, jwt: String) : LikeResponse
}
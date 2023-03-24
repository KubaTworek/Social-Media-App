package com.example.articles.service

import com.example.articles.model.dto.AuthorDTO

interface AuthorApiService {
    fun getAuthorById(authorId: Int): AuthorDTO
}

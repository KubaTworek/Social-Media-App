package com.example.articles.client.service

import com.example.articles.model.dto.AuthorDTO

interface AuthorApiService {
    fun getAuthorById(authorId: Int): AuthorDTO
}

package com.example.notifications.client.service

import com.example.notifications.model.dto.AuthorDTO

interface AuthorApiService {
    fun getAuthorById(authorId: Int): AuthorDTO
}

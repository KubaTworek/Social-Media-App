package com.example.articles.client.service

import com.example.articles.model.dto.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetails(jwt: String): UserDetailsDTO
}

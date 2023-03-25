package com.example.notifications.client.service

import com.example.notifications.model.dto.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetails(jwt: String): UserDetailsDTO
}

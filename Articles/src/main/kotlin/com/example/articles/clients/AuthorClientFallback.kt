package com.example.articles.clients

import com.example.articles.controller.AuthorRequest
import feign.FeignException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier("AuthorClientFallback")
class AuthorClientFallback : AuthorClient {
    override fun getAuthor(firstName: String, lastName: String): ResponseEntity<String> {
        try {
            return getAuthor(firstName, lastName)
        } catch (exc: FeignException) {
            if (exc.status() == HttpStatus.NOT_FOUND.value()) {
                return saveAuthor(AuthorRequest(firstName, lastName))
            }
            throw exc
        }
    }

    override fun saveAuthor(authorRequest: AuthorRequest): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}
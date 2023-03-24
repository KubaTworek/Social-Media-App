package com.example.authorization.client

import com.example.authorization.controller.dto.AuthorRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(name = "authors", url = "http://authors:2112/api", fallback = AuthorClientFallback::class)
@Qualifier("AuthorClient")
interface AuthorClient {
    @PostMapping("/")
    fun createAuthor(@RequestBody theAuthor: AuthorRequest): ResponseEntity<Void>

    @DeleteMapping("/{authorId}")
    fun deleteAuthorById(@PathVariable authorId: Int): ResponseEntity<Void>

    @GetMapping("/username/{username}")
    fun getAuthorByUsername(@PathVariable username: String): ResponseEntity<String>
}
package com.example.articles.clients

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@FeignClient(name = "authors", url = "http://authors:2112/api/authors", fallback = AuthorClientFallback::class)
@Qualifier("AuthorClient")
interface AuthorClient {
    @GetMapping("/id/{authorId}")
    fun getAuthor(@PathVariable authorId: Int): ResponseEntity<String>
}
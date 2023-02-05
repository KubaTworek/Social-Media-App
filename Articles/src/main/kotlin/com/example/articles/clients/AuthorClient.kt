package com.example.articles.clients

import com.example.articles.controller.AuthorRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@FeignClient(name = "authors", url = "http://authors:2112/api/authors", fallback = AuthorClientFallback::class)
@Qualifier("AuthorClient")
interface AuthorClient {
    @GetMapping("/name/{firstName}/{lastName}")
    fun getAuthor(@PathVariable firstName: String, @PathVariable lastName: String): ResponseEntity<String>

    @PostMapping("/")
    fun saveAuthor(@RequestBody authorRequest: AuthorRequest): ResponseEntity<String>
}
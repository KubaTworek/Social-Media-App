package com.example.authors.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "articles", url = "http://authors:2111/api/articles", fallback = ArticleClientFallback::class)
@Qualifier("ArticleClient")
interface ArticleClient {
    @GetMapping("/author/{authorId}")
    fun getArticlesByAuthor(@PathVariable authorId: Int): ResponseEntity<String>
}
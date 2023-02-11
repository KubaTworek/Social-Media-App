package com.example.magazines.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "articles", url = "http://articles:2111/api/articles")
@Qualifier("ArticleClient")
interface ArticleClient {
    @GetMapping("/magazine/{magazineId}")
    fun getArticlesByMagazine(@PathVariable magazineId: Int): ResponseEntity<String>
}
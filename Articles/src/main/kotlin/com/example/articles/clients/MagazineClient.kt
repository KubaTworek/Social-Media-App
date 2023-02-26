package com.example.articles.clients

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "magazines", url = "http://magazines:2113/api", fallback = MagazineClientFallback::class)
@Qualifier("MagazineClient")
interface MagazineClient {
    @GetMapping("/id/{authorId}")
    fun getMagazine(@PathVariable authorId: Int): ResponseEntity<String>
}

package com.example.articles.clients

import com.example.articles.controller.MagazineRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "magazines", url = "http://magazines:2113/api/magazines", fallback = MagazineClientFallback::class)
@Qualifier("MagazineClient")
interface MagazineClient {
    @GetMapping("/id/{authorId}")
    fun getMagazine(@PathVariable authorId: Int): ResponseEntity<String>

    @PostMapping("/")
    fun saveMagazine(@RequestBody magazineRequest: MagazineRequest): ResponseEntity<String>
}

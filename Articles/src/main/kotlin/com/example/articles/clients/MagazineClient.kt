package com.example.articles.clients

import com.example.articles.controller.MagazineRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "magazines", url = "http://localhost:2113/api/magazines", fallback = MagazineClientFallback::class)
@Qualifier("MagazineClient")
interface MagazineClient {
    @GetMapping("/name/{name}")
    fun getMagazine(@PathVariable name: String): ResponseEntity<String>

    @PostMapping
    fun saveMagazine(@RequestBody magazineRequest: MagazineRequest): ResponseEntity<String>
}

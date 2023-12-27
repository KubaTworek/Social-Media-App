package pl.jakubtworek.common.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.common.model.AuthorRequest
import java.util.*

@FeignClient(name = "authors", url = "http://authors:2112/api", fallback = AuthorClientFallback::class)
interface AuthorClient {
    @GetMapping("/id/{authorId}")
    fun getAuthorById(@PathVariable authorId: Int): ResponseEntity<String>

    @PostMapping("/")
    fun createAuthor(@RequestBody theAuthor: AuthorRequest): ResponseEntity<Void>

    @DeleteMapping("/{authorId}")
    fun deleteAuthorById(@PathVariable authorId: Int): ResponseEntity<Void>

    @GetMapping("/username/{username}")
    fun getAuthorByUsername(@PathVariable username: String): ResponseEntity<String>

    @DeleteMapping("/authorId/{authorId}")
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int): ResponseEntity<String>
}

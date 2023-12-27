package pl.jakubtworek.common.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "articles", url = "http://articles:2111/api")
interface ArticleClient {
    @DeleteMapping("/authorId/{authorId}")
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int): ResponseEntity<String>

    @GetMapping("/id/{articleId}")
    fun getArticleById(@PathVariable articleId: Int): ResponseEntity<String>

    @GetMapping("/author/{authorId}")
    fun getArticlesByAuthor(@PathVariable authorId: Int): ResponseEntity<String>
}

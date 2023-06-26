package com.example.articles.controller

import com.example.articles.controller.dto.ArticleRequest
import com.example.articles.controller.dto.ArticleResponse
import com.example.articles.service.ArticleService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class ArticleController(private val articleService: ArticleService) {

    // EXTERNAL
    @GetMapping("/")
    fun getArticlesOrderByDateDesc(): List<ArticleResponse> =
        articleService.findAllOrderByCreatedTimeDesc()

    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByKeyword(@PathVariable keyword: String) =
        articleService.findAllByKeyword(keyword)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(
        @RequestHeader("Authorization") jwt: String,
        @RequestBody theArticle: ArticleRequest
    ) = articleService.save(theArticle, jwt)

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteArticle(
        @RequestHeader("Authorization") jwt: String,
        @PathVariable articleId: Int
    ) = articleService.deleteById(articleId, jwt)

    // INTERNAL
    @GetMapping("/id/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesById(@PathVariable articleId: Int) =
        articleService.findById(articleId)

    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByAuthor(@PathVariable authorId: Int) =
        articleService.findAllByAuthorId(authorId)

    @DeleteMapping("/authorId/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int) =
        articleService.deleteByAuthorId(authorId)
}

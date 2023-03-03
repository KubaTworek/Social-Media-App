package com.example.articles.controller

import com.example.articles.model.dto.ArticleDTO
import com.example.articles.service.ArticleService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
class ArticleController(private val articleService: ArticleService) {
    @GetMapping("/")
    fun articlesOrderByDateDesc(): List<ArticleResponse> =
        articleService.findAllOrderByDateDesc()

    @GetMapping("/id/{articleId}")
    fun getArticleById(@PathVariable articleId: Int): ArticleResponse =
        articleService.findById(articleId)

    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByAuthor(@PathVariable authorId: Int): List<ArticleDTO> =
        articleService.findAllByAuthorId(authorId)

    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByKeyword(@PathVariable keyword: String): List<ArticleResponse> =
        articleService.findAllByKeyword(keyword)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(@RequestBody theArticle: ArticleRequest): Unit =
        articleService.save(theArticle)

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteArticle(@PathVariable articleId: Int): Unit =
        articleService.deleteById(articleId)

    @DeleteMapping("/authorId/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int): Unit =
        articleService.deleteByAuthorId(authorId)
}
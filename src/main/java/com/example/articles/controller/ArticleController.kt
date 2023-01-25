package com.example.articles.controller

import com.example.articles.entity.Article
import com.example.articles.service.ArticleService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RequestMapping("/articles")
@RestController
@RequiredArgsConstructor
class ArticleController(private val articleService: ArticleService) {

    @GetMapping
    fun articlesOrderByDateDesc() = articleService.findAllOrderByDateDesc()
        .stream()
        .map(Article::toResponse)
        .toList()

    @GetMapping("/{articleId}")
    fun getArticleById(@PathVariable articleId: Int) = articleService.findById(articleId).toResponse()

    @GetMapping("/{keyword}")
    fun getArticlesByKeyword(@PathVariable keyword: String) = articleService.findAllByKeyword(keyword)
        .stream()
        .map(Article::toResponse)
        .toList()

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(@RequestBody theArticle: ArticleRequest) = articleService.save(theArticle)

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(@PathVariable articleId: Int) = articleService.deleteById(articleId)
}
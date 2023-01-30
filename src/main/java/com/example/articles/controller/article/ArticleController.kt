package com.example.articles.controller.article

import com.example.articles.entity.ArticlePost
import com.example.articles.errors.ArticleNotFoundException
import com.example.articles.service.ArticleService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RequestMapping("/articles")
@RestController
@RequiredArgsConstructor
class ArticleController(private val articleService: ArticleService) {

    @CrossOrigin
    @GetMapping
    fun articlesOrderByDateDesc() = articleService.findAllOrderByDateDesc()
        .stream()
        .map(ArticlePost::toResponse)
        .toList()

    @CrossOrigin
    @GetMapping("/id/{articleId}")
    fun getArticleById(@PathVariable articleId: Int) = articleService.findById(articleId)
        .orElseThrow { ArticleNotFoundException("ArticlePost id not found - $articleId") }
        .toResponse()

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByKeyword(@PathVariable keyword: String) = articleService.findAllByKeyword(keyword)
        .stream()
        .map(ArticlePost::toResponse)
        .toList()

    @CrossOrigin
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(@RequestBody theArticle: ArticleRequest) = articleService.save(theArticle)

    @CrossOrigin
    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(@PathVariable articleId: Int) = articleService.deleteById(articleId)
}
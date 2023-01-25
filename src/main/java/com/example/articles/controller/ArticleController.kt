package com.example.articles.controller

import com.example.articles.entity.Article
import com.example.articles.service.ArticleService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RequestMapping("/")
@RestController
@RequiredArgsConstructor
class ArticleController(private val articleService: ArticleService) {

    @GetMapping("/articles")
    fun articlesOrderByDateDesc() = articleService.findAllOrderByDateDesc()

    @GetMapping("/articles/{articleId}")
    fun getArticleById(@PathVariable articleId: Int) = articleService.findById(articleId)

    @GetMapping("/articles/{keyword}")
    fun getArticlesByKeyword(@PathVariable keyword: String) = articleService.findAllByKeyword(keyword)

    @PostMapping("/articles")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(@RequestBody theArticle: ArticleRequest) = articleService.save(theArticle)

    @DeleteMapping("/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(@PathVariable articleId: Int) = articleService.deleteById(articleId)
}
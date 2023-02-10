package com.example.articles.controller

import com.example.articles.exception.ArticleNotFoundException
import com.example.articles.model.Article
import com.example.articles.service.ArticleService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/articles")
@RestController
@RequiredArgsConstructor
class ArticleController(private val articleService: ArticleService) {
    @CrossOrigin
    @GetMapping("/")
    fun articlesOrderByDateDesc() = articleService.findAllOrderByDateDesc()

    @CrossOrigin
    @GetMapping("/id/{articleId}")
    fun getArticleById(@PathVariable articleId: Int) = articleService.findById(articleId)
        .orElseThrow { ArticleNotFoundException("ArticlePost id not found - $articleId") }

    @CrossOrigin
    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByAuthor(@PathVariable authorId: Int) = articleService.findAllByAuthorId(authorId)

    @CrossOrigin
    @GetMapping("/magazine/{magazineId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByMagazine(@PathVariable magazineId: Int) = articleService.findAllByMagazineId(magazineId)

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByKeyword(@PathVariable keyword: String) = articleService.findAllByKeyword(keyword)

    @CrossOrigin
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(@RequestBody theArticle: ArticleRequest) = articleService.save(theArticle)

    @CrossOrigin
    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(@PathVariable articleId: Int) = articleService.deleteById(articleId)
}
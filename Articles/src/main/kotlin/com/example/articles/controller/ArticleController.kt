package com.example.articles.controller

import com.example.articles.model.dto.ArticleDTO
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
    fun articlesOrderByDateDesc(): List<ArticleResponse> =
        articleService.findAllOrderByDateDesc()

    @CrossOrigin
    @GetMapping("/id/{articleId}")
    fun getArticleById(@PathVariable articleId: Int): ArticleResponse =
        articleService.findById(articleId)

    @CrossOrigin
    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByAuthor(@PathVariable authorId: Int): List<ArticleDTO> =
        articleService.findAllByAuthorId(authorId)

    @CrossOrigin
    @GetMapping("/magazine/{magazineId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByMagazine(@PathVariable magazineId: Int): List<ArticleDTO> =
        articleService.findAllByMagazineId(magazineId)

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByKeyword(@PathVariable keyword: String): List<ArticleResponse> =
        articleService.findAllByKeyword(keyword)

    @CrossOrigin
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(@RequestBody theArticle: ArticleRequest): Unit =
        articleService.save(theArticle)

    @CrossOrigin
    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(@PathVariable articleId: Int): Unit =
        articleService.deleteById(articleId)

    @CrossOrigin
    @DeleteMapping("/authorId/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int): Unit =
        articleService.deleteByAuthorId(authorId)

    @CrossOrigin
    @DeleteMapping("/magazineId{magazineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticlesByMagazineId(@PathVariable magazineId: Int): Unit =
        articleService.deleteByMagazineId(magazineId)
}
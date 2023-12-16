package pl.jakubtworek.articles.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.model.dto.ArticleDTO
import pl.jakubtworek.articles.service.ArticleService

@RequestMapping("/api")
@RestController
class ArticleController(private val articleService: ArticleService) {

    // EXTERNAL
    @GetMapping("/")
    fun getArticlesOrderByDateDesc(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): List<ArticleResponse> {
        return articleService.findAllOrderByCreatedTimeDesc(page, size)
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(
        @RequestHeader("Authorization") jwt: String,
        @RequestBody theArticle: ArticleRequest
    ) = articleService.save(theArticle, jwt)

    @PutMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateArticle(
        @RequestHeader("Authorization") jwt: String,
        @RequestBody theArticle: ArticleRequest,
        @PathVariable articleId: Int
    ) = articleService.update(theArticle, articleId, jwt)

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteArticle(
        @RequestHeader("Authorization") jwt: String,
        @PathVariable articleId: Int
    ) = articleService.deleteById(articleId, jwt)

    // INTERNAL
    @GetMapping("/id/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticleById(@PathVariable articleId: Int) =
        articleService.findById(articleId)

    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticlesByAuthor(@PathVariable authorId: Int) =
        articleService.findAllByAuthorId(authorId)

    @DeleteMapping("/authorId/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int) =
        articleService.deleteByAuthorId(authorId)
}

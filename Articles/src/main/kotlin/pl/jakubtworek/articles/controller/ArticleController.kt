package pl.jakubtworek.articles.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.articles.controller.dto.*
import pl.jakubtworek.articles.service.ArticleService
import pl.jakubtworek.common.Constants.AUTHORIZATION_HEADER
import pl.jakubtworek.common.model.ArticleDTO

@RequestMapping("/articles")
@RestController
class ArticleController(
    private val articleService: ArticleService
) {

    // POST
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @RequestBody request: ArticleCreateRequest
    ): ResponseEntity<ArticleResponse> = ResponseEntity.status(HttpStatus.CREATED)
        .body(articleService.saveArticle(request, jwt))

    @PostMapping("/{articleId}/like", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun likeArticle(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @PathVariable articleId: Int
    ): ResponseEntity<LikeActionResponse> = ResponseEntity.status(HttpStatus.CREATED)
        .body(articleService.handleLikeAction(articleId, jwt))

    // PUT
    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE]) // "/{authorId}"
    @ResponseStatus(HttpStatus.OK)
    fun updateArticle(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @RequestBody request: ArticleUpdateRequest
    ) = articleService.updateArticle(request, jwt)

    // GET
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getLatestArticles(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<List<ArticleResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.getLatestArticles(page, size, jwt))

    @GetMapping("/authors/followed", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getLatestFollowingArticles(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<List<ArticleResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.getLatestFollowingArticles(page, size, jwt))

    @GetMapping("/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticleDetailsById(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @PathVariable articleId: Int
    ): ResponseEntity<ArticleDetailsResponse> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.getArticle(articleId, jwt))

    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticlesByAuthorById(
        @PathVariable authorId: Int
    ): ResponseEntity<List<ArticleDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.getArticlesByAuthorId(authorId))

    @GetMapping("/id/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticleById(
        @PathVariable articleId: Int
    ): ResponseEntity<ArticleDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.getArticleById(articleId))

    // DELETE
    @DeleteMapping("/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticleById(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @PathVariable articleId: Int
    ) = articleService.deleteArticleById(articleId, jwt)

    @DeleteMapping("/authorId/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE]) // "/author/{authorId}"
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticlesByAuthorId(
        @PathVariable authorId: Int
    ) = articleService.deleteArticlesByAuthorId(authorId)
}

package pl.jakubtworek.articles.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse
import pl.jakubtworek.articles.service.ArticleService
import pl.jakubtworek.common.Constants.AUTHORIZATION_HEADER
import pl.jakubtworek.common.model.ArticleDTO

@RequestMapping("/api")
@RestController
class ArticleController(
    private val articleService: ArticleService
) {

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticlesOrderedByDateDesc(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<List<ArticleResponse>> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.findAllOrderByCreatedTimeDesc(page, size))

    @GetMapping("/author/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticlesByAuthor(
        @PathVariable authorId: Int
    ): ResponseEntity<List<ArticleDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.findAllByAuthorId(authorId))

    @GetMapping("/id/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticleById(
        @PathVariable articleId: Int
    ): ResponseEntity<ArticleDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(articleService.findById(articleId))

    @PostMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun saveArticle(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @RequestBody theArticle: ArticleRequest
    ) = articleService.save(theArticle, jwt)

    @PutMapping("/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun updateArticle(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @RequestBody theArticle: ArticleRequest,
        @PathVariable articleId: Int
    ) = articleService.update(theArticle, articleId, jwt)

    @PostMapping("/like/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun likeArticle(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @PathVariable articleId: Int
    ): ResponseEntity<LikeResponse> = ResponseEntity.status(HttpStatus.CREATED)
        .body(articleService.like(articleId, jwt))

    @DeleteMapping("/{articleId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticleById(
        @RequestHeader(AUTHORIZATION_HEADER) jwt: String,
        @PathVariable articleId: Int
    ) = articleService.deleteById(articleId, jwt)

    @DeleteMapping("/authorId/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticlesByAuthorId(
        @PathVariable authorId: Int
    ) = articleService.deleteByAuthorId(authorId)
}

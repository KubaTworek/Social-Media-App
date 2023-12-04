package pl.jakubtworek.articles.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.articles.service.LikeService

@RequestMapping("/api/like")
@RestController
class LikeController(private val likeService: LikeService) {

    @PostMapping("/{articleId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun likeArticle(
        @RequestHeader("Authorization") jwt: String,
        @PathVariable articleId: Int
    ) = likeService.like(articleId, jwt)

    // deprecated
    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun showLikeInfo(
        @PathVariable articleId: Int
    ) = likeService.getLikeInfo(articleId)
}
package pl.jakubtworek.authors.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "articles", url = "http://articles:2111/api")
@Qualifier("ArticleClient")
interface ArticleClient {
    @DeleteMapping("/authorId/{authorId}")
    fun deleteArticlesByAuthorId(@PathVariable authorId: Int): ResponseEntity<String>
}

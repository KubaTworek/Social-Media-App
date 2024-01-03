package pl.jakubtworek.authors.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.service.AuthorService
import pl.jakubtworek.common.Constants
import pl.jakubtworek.common.model.AuthorDTO

@RequestMapping("/api")
@RestController
class AuthorController(
    private val authorService: AuthorService
) {

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAllAuthors(
        @RequestHeader(Constants.AUTHORIZATION_HEADER) jwt: String
    ): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.getAllAuthors(jwt))

    @GetMapping("/following/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorsFollowing(
        @PathVariable authorId: Int
    ): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.getFollowing(authorId))

    @GetMapping("/followers/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorsFollowed(
        @PathVariable authorId: Int
    ): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.getFollowers(authorId))

    @GetMapping("/id/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorById(
        @PathVariable authorId: Int
    ): ResponseEntity<AuthorDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.getAuthorById(authorId))

    @GetMapping("/username/{username}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorByUsername(
        @PathVariable username: String
    ): ResponseEntity<AuthorDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.getAuthorByUsername(username))

    @PostMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(
        @RequestBody request: AuthorRequest
    ) = authorService.saveAuthor(request)

    @PutMapping("/follow/{followingId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun followAuthor(
        @RequestHeader(Constants.AUTHORIZATION_HEADER) jwt: String,
        @PathVariable followingId: Int
    ) = authorService.followAuthor(followingId, jwt)

    @PutMapping("/unfollow/{followingId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun unfollowAuthor(
        @RequestHeader(Constants.AUTHORIZATION_HEADER) jwt: String,
        @PathVariable followingId: Int
    ) = authorService.unfollowAuthor(followingId, jwt)

    @DeleteMapping("/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthorById(
        @PathVariable authorId: Int
    ) = authorService.deleteAuthorById(authorId)
}

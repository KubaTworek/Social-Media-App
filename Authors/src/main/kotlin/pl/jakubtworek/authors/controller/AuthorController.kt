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
    fun getAuthors(
        @RequestHeader(Constants.AUTHORIZATION_HEADER) jwt: String
    ): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.findAll(jwt))

    @GetMapping("/following/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorsFollowing(
        @PathVariable authorId: Int
    ): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.findFollowing(authorId))

    @GetMapping("/followers/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorsFollowed(
        @PathVariable authorId: Int
    ): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.findFollowers(authorId))

    @GetMapping("/id/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorById(
        @PathVariable authorId: Int
    ): ResponseEntity<AuthorDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.findById(authorId))

    @GetMapping("/username/{username}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorByUsername(
        @PathVariable username: String
    ): ResponseEntity<AuthorDTO> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.findByUsername(username))

    @PostMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(
        @RequestBody theAuthor: AuthorRequest
    ) = authorService.save(theAuthor)

    @PutMapping("/follow/{followingId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun follow(
        @RequestHeader(Constants.AUTHORIZATION_HEADER) jwt: String,
        @PathVariable followingId: Int
    ) = authorService.follow(followingId, jwt)

    @PutMapping("/unfollow/{followingId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun unfollow(
        @RequestHeader(Constants.AUTHORIZATION_HEADER) jwt: String,
        @PathVariable followingId: Int
    ) = authorService.unfollow(followingId, jwt)

    @DeleteMapping("/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(
        @PathVariable authorId: Int
    ) = authorService.deleteById(authorId)
}

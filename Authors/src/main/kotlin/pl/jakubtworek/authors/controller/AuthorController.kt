package pl.jakubtworek.authors.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.service.AuthorService
import pl.jakubtworek.common.model.AuthorDTO

@RequestMapping("/api")
@RestController
class AuthorController(
    private val authorService: AuthorService
) {

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAuthors(): ResponseEntity<List<AuthorDTO>> = ResponseEntity.status(HttpStatus.OK)
        .body(authorService.findAll())

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

    @DeleteMapping("/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(
        @PathVariable authorId: Int
    ) = authorService.deleteById(authorId)
}

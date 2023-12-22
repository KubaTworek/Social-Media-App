package pl.jakubtworek.authors.controller

import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RequestMapping("/api")
@RestController
class AuthorController(private val authorService: AuthorService) {

    // EXTERNAL
    @GetMapping("/")
    fun getAuthors() =
        authorService.findAll()

    // INTERNAL
    @GetMapping("/id/{authorId}")
    fun getAuthorById(@PathVariable authorId: Int) =
        authorService.findById(authorId)

    @GetMapping("/username/{username}")
    fun getAuthorByUsername(@PathVariable username: String) =
        authorService.findByUsername(username)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(@RequestBody theAuthor: AuthorRequest) =
        authorService.save(theAuthor)

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(@PathVariable authorId: Int) =
        authorService.deleteById(authorId)
}

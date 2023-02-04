package com.example.authors.controller

import com.example.authors.exception.AuthorNotFoundException
import com.example.authors.model.Author
import com.example.authors.service.AuthorService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/authors")
@RestController
@RequiredArgsConstructor
class AuthorController(private val authorService: AuthorService) {

    @CrossOrigin
    @GetMapping("/")
    fun getAllAuthors() = authorService.findAllAuthors()
        .stream()
        .map(Author::toResponse)
        .toList()

    @CrossOrigin
    @GetMapping("/id/{authorId}")
    fun getAuthorById(@PathVariable authorId: Int) = authorService.findById(authorId)
        .orElseThrow { AuthorNotFoundException("AuthorPost id not found - $authorId") }
        .toResponse()

    @CrossOrigin
    @GetMapping("/name/{firstName}/{lastName}")
    fun getAuthorByName(@PathVariable firstName: String, @PathVariable lastName: String) =
        authorService.findByName(firstName, lastName)
            .orElseThrow { AuthorNotFoundException("Author not found - $firstName $lastName") }
            .toDTO()

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuthorsByKeyword(@PathVariable keyword: String) = authorService.findAllByKeyword(keyword)
        .stream()
        .map(Author::toResponse)
        .toList()

    @CrossOrigin
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(@RequestBody theAuthor: AuthorRequest) = authorService.save(theAuthor)

    @CrossOrigin
    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAuthor(@PathVariable authorId: Int) = authorService.deleteById(authorId)
}
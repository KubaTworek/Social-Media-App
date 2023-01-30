package com.example.articles.controller.author

import com.example.articles.entity.AuthorPost
import com.example.articles.errors.AuthorNotFoundException
import com.example.articles.service.AuthorService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RequestMapping("/api/authors")
@RestController
@RequiredArgsConstructor
class AuthorController(private val authorService: AuthorService) {

    @CrossOrigin
    @GetMapping
    fun getAllAuthors() = authorService.findAllAuthors()
        .stream()
        .map(AuthorPost::toResponse)
        .toList()

    @CrossOrigin
    @GetMapping("/id/{authorId}")
    fun getAuthorById(@PathVariable authorId: Int) = authorService.findById(authorId)
        .orElseThrow { AuthorNotFoundException("AuthorPost id not found - $authorId") }
        .toResponse()

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuthorsByKeyword(@PathVariable keyword: String) = authorService.findAllByKeyword(keyword)
        .stream()
        .map(AuthorPost::toResponse)
        .toList()

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(@RequestBody theAuthor: AuthorRequest) = authorService.save(theAuthor)

    @CrossOrigin
    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAuthor(@PathVariable authorId: Int) = authorService.deleteById(authorId)
}
package com.example.authors.controller

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
    fun getAllAuthors() =
        authorService.findAllAuthors()

    @CrossOrigin
    @GetMapping("/id/{authorId}")
    fun getAuthorById(@PathVariable authorId: Int) =
        authorService.findById(authorId)

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuthorsByKeyword(@PathVariable keyword: String) =
        authorService.findAllByKeyword(keyword)

    @CrossOrigin
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(@RequestBody theAuthor: AuthorRequest) =
        authorService.save(theAuthor)

    @CrossOrigin
    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(@PathVariable authorId: Int) =
        authorService.deleteById(authorId)
}
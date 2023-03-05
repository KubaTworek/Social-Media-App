package com.example.authors.controller

import com.example.authors.service.AuthorService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
class AuthorController(private val authorService: AuthorService) {

    @GetMapping("/")
    fun getAllAuthors() =
        authorService.findAllAuthors()

    @GetMapping("/id/{authorId}")
    fun getAuthorById(@PathVariable authorId: Int) =
        authorService.findById(authorId)

    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuthorsByKeyword(@PathVariable keyword: String) =
        authorService.findAllByKeyword(keyword)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAuthor(@RequestBody theAuthor: AuthorRequest) =
        authorService.save(theAuthor)

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(@PathVariable authorId: Int) =
        authorService.deleteById(authorId)

    @DeleteMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthorByUsername(@PathVariable username: String) =
        authorService.deleteByUsername(username)
}
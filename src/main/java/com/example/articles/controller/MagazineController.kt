package com.example.articles.controller

import com.example.articles.entity.Magazine
import com.example.articles.errors.ArticleNotFoundException
import com.example.articles.service.MagazineService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import kotlin.streams.toList

@RequestMapping("/magazines")
@RestController
@RequiredArgsConstructor
class MagazineController(private val magazineService: MagazineService) {

    @CrossOrigin
    @GetMapping
    fun getAllMagazines() = magazineService.findAllMagazines()
        .stream()
        .map(Magazine::toResponse)
        .toList()

    @CrossOrigin
    @GetMapping("/id/{magazineId}")
    fun getMagazineById(@PathVariable magazineId: Int) = magazineService.findById(magazineId)
        .orElseThrow { ArticleNotFoundException("Magazine id not found - $magazineId") }
        .toResponse()

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMagazinesByKeyword(@PathVariable keyword: String) = magazineService.findAllByKeyword(keyword)
        .stream()
        .map(Magazine::toResponse)
        .toList()

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMagazine(@RequestBody theMagazine: MagazineRequest) = magazineService.save(theMagazine)

    @CrossOrigin
    @DeleteMapping("/{magazineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMagazine(@PathVariable magazineId: Int) = magazineService.deleteById(magazineId)
}
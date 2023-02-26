package com.example.magazines.controller

import com.example.magazines.service.MagazineService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
class MagazineController(private val magazineService: MagazineService) {

    @GetMapping("/")
    fun getAllMagazines() =
        magazineService.findAllMagazines()

    @GetMapping("/id/{magazineId}")
    fun getMagazineById(@PathVariable magazineId: Int) =
        magazineService.findById(magazineId)

    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMagazinesByKeyword(@PathVariable keyword: String) =
        magazineService.findAllByKeyword(keyword)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMagazine(@RequestBody theMagazine: MagazineRequest) =
        magazineService.save(theMagazine)

    @DeleteMapping("/{magazineId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteMagazine(@PathVariable magazineId: Int) =
        magazineService.deleteById(magazineId)
}
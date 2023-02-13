package com.example.magazines.controller

import com.example.magazines.service.MagazineService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/magazines")
@RestController
@RequiredArgsConstructor
class MagazineController(private val magazineService: MagazineService) {

    @CrossOrigin
    @GetMapping("/")
    fun getAllMagazines() =
        magazineService.findAllMagazines()

    @CrossOrigin
    @GetMapping("/id/{magazineId}")
    fun getMagazineById(@PathVariable magazineId: Int) =
        magazineService.findById(magazineId)

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMagazinesByKeyword(@PathVariable keyword: String) =
        magazineService.findAllByKeyword(keyword)

    @CrossOrigin
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMagazine(@RequestBody theMagazine: MagazineRequest) =
        magazineService.save(theMagazine)

    @CrossOrigin
    @DeleteMapping("/{magazineId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteMagazine(@PathVariable magazineId: Int) =
        magazineService.deleteById(magazineId)
}
package com.example.magazines.controller

import com.example.magazines.exception.MagazineNotFoundException
import com.example.magazines.model.Magazine
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
    fun getAllMagazines() = magazineService.findAllMagazines()
        .stream()
        .map(Magazine::toResponse)
        .toList()

    @CrossOrigin
    @GetMapping("/id/{magazineId}")
    fun getMagazineById(@PathVariable magazineId: Int) = magazineService.findById(magazineId)
        .orElseThrow { MagazineNotFoundException("MagazinePost id not found - $magazineId") }
        .toResponse()

    @CrossOrigin
    @GetMapping("/name/{magazineName}")
    fun getMagazineByName(@PathVariable magazineName: String) = magazineService.findByName(magazineName)
        .orElseThrow { MagazineNotFoundException("Magazine not found - $magazineName") }
        .toDTO()

    @CrossOrigin
    @GetMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMagazinesByKeyword(@PathVariable keyword: String) = magazineService.findAllByKeyword(keyword)
        .stream()
        .map(Magazine::toResponse)
        .toList()

    @CrossOrigin
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMagazine(@RequestBody theMagazine: MagazineRequest) = magazineService.save(theMagazine)

    @CrossOrigin
    @DeleteMapping("/{magazineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMagazine(@PathVariable magazineId: Int) = magazineService.deleteById(magazineId)
}
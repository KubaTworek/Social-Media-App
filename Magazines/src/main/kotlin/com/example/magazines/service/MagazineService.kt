package com.example.magazines.service

import com.example.magazines.controller.MagazineRequest
import com.example.magazines.model.Magazine
import java.util.*

interface MagazineService {
    fun findAllMagazines(): List<Magazine>
    fun findById(theId: Int): Optional<Magazine>
    fun findByName(theName: String): Optional<Magazine>
    fun findAllByKeyword(theKeyword: String): List<Magazine>
    fun save(theMagazine: MagazineRequest): Magazine
    fun deleteById(theId: Int)
}
package com.example.articles.service

import com.example.articles.controller.MagazineRequest
import com.example.articles.entity.Magazine
import java.util.*

interface MagazineService {
    fun findAllMagazines(): List<Magazine>
    fun findById(theId: Int): Optional<Magazine>
    fun findAllByKeyword(theKeyword: String): List<Magazine>
    fun save(theMagazine: MagazineRequest)
    fun deleteById(theId: Int)
}
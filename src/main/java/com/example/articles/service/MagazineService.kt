package com.example.articles.service

import com.example.articles.controller.magazine.MagazineRequest
import com.example.articles.entity.MagazinePost
import java.util.*

interface MagazineService {
    fun findAllMagazines(): List<MagazinePost>
    fun findById(theId: Int): Optional<MagazinePost>
    fun findAllByKeyword(theKeyword: String): List<MagazinePost>
    fun save(theMagazine: MagazineRequest)
    fun deleteById(theId: Int)
}
package com.example.magazines.service

import com.example.magazines.controller.MagazineRequest
import com.example.magazines.controller.MagazineResponse
import com.example.magazines.model.dto.MagazineDTO

interface MagazineService {
    fun findAllMagazines(): List<MagazineResponse>
    fun findById(theId: Int): MagazineDTO
    fun findAllByKeyword(theKeyword: String): List<MagazineResponse>
    fun save(theMagazine: MagazineRequest)
    fun deleteById(theId: Int)
}
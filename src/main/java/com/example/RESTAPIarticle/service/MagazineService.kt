package com.example.RESTAPIarticle.service

import com.example.RESTAPIarticle.entity.Magazine

interface MagazineService {
    fun findById(theId: Int): Magazine?
    fun save(theMagazine: Magazine)
    fun findByName(theName: String): Magazine?
}
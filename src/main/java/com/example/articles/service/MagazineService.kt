package com.example.articles.service

import com.example.articles.entity.Magazine

interface MagazineService {
    fun findById(theId: Int): Magazine?
    fun save(theMagazine: Magazine)
    fun findByName(theName: String): Magazine?
}
package com.example.articles.clients

import com.example.articles.controller.MagazineRequest

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier("MagazineClientFallback")
class MagazineClientFallback : MagazineClient {
    override fun getMagazine(name: String): ResponseEntity<String> {
        return saveMagazine(MagazineRequest(name, "test"))
    }

    override fun saveMagazine(magazineRequest: MagazineRequest): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}

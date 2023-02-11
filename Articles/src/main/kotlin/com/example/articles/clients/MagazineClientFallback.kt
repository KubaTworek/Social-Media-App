package com.example.articles.clients

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier("MagazineClientFallback")
class MagazineClientFallback : MagazineClient {
    override fun getMagazine(magazineId: Int): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)
}

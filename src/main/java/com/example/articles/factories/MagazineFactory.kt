package com.example.articles.factories

import com.example.articles.controller.magazine.MagazineRequest
import com.example.articles.entity.MagazinePost
import org.springframework.stereotype.Component
import java.util.Collections.emptyList

@Component
class MagazineFactory {
    fun createMagazine(name: String): MagazinePost {
        return MagazinePost(
            0,
            name,
            emptyList()
        )
    }

    fun createMagazine(magazineRequest: MagazineRequest): MagazinePost {
        return MagazinePost(
            0,
            magazineRequest.magazineName,
            emptyList()
        )
    }
}
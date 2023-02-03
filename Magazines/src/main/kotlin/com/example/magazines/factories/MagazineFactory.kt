package com.example.magazines.factories

import com.example.magazines.controller.MagazineRequest
import com.example.magazines.model.Magazine
import org.springframework.stereotype.Component
import java.util.Collections.emptyList

@Component
class MagazineFactory {
    fun createMagazine(name: String): Magazine {
        return Magazine(
            0,
            name,
            emptyList()
        )
    }

    fun createMagazine(magazineRequest: MagazineRequest): Magazine {
        return Magazine(
            0,
            magazineRequest.magazineName,
            emptyList()
        )
    }
}
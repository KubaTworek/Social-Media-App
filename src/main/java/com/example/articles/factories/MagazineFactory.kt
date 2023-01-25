package com.example.articles.factories

import com.example.articles.entity.Author
import com.example.articles.entity.Magazine
import org.springframework.stereotype.Component
import java.util.*
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
}
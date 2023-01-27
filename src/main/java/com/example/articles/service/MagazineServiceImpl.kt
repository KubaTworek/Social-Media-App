package com.example.articles.service

import com.example.articles.controller.magazine.MagazineRequest
import com.example.articles.entity.Magazine
import com.example.articles.factories.MagazineFactory
import com.example.articles.repository.MagazineRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.util.*
import kotlin.streams.toList

@Service
@RequiredArgsConstructor
class MagazineServiceImpl(
    private val magazineRepository: MagazineRepository,
    private val magazineFactory: MagazineFactory
) : MagazineService {
    override fun findAllMagazines(): List<Magazine> {
        return magazineRepository.findAll()
    }

    override fun findById(theId: Int): Optional<Magazine> {
        return magazineRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<Magazine> {
        val magazines: List<Magazine> = magazineRepository.findAll()
        return magazines.stream()
            .filter { magazines: Magazine ->
                magazines.name.contains(theKeyword)
            }
            .toList()
    }

    override fun save(theMagazine: MagazineRequest) {
        val magazine = magazineFactory.createMagazine(theMagazine)

        magazineRepository.save(magazine)
    }

    override fun deleteById(theId: Int) {
        magazineRepository.deleteById(theId)
    }
}
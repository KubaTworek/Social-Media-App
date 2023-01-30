package com.example.articles.service

import com.example.articles.controller.magazine.MagazineRequest
import com.example.articles.entity.MagazinePost
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
    override fun findAllMagazines(): List<MagazinePost> {
        return magazineRepository.findAll()
    }

    override fun findById(theId: Int): Optional<MagazinePost> {
        return magazineRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<MagazinePost> {
        val magazines: List<MagazinePost> = magazineRepository.findAll()
        return magazines.stream()
            .filter { magazines: MagazinePost ->
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
package com.example.magazines.service

import com.example.magazines.controller.MagazineRequest
import com.example.magazines.factories.MagazineFactory
import com.example.magazines.model.Magazine
import com.example.magazines.repository.MagazineRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.util.*

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

    override fun findByName(theName: String): Optional<Magazine> {
        return magazineRepository.findByName(theName)
    }

    override fun findAllByKeyword(theKeyword: String): List<Magazine> {
        val magazines: List<Magazine> = magazineRepository.findAll()
        return magazines.stream()
            .filter { it.name.contains(theKeyword) }
            .toList()
    }

    override fun save(theMagazine: MagazineRequest): Magazine {
        val magazine = magazineFactory.createMagazine(theMagazine)

        return magazineRepository.save(magazine)
    }

    override fun deleteById(theId: Int) {
        magazineRepository.deleteById(theId)
    }
}
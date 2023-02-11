package com.example.magazines.service

import com.example.magazines.controller.MagazineRequest
import com.example.magazines.controller.MagazineResponse
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
    override fun findAllMagazines(): List<MagazineResponse> {
        val magazines = magazineRepository.findAll()
        val magazinesResponseList = mutableListOf<MagazineResponse>()
        for (magazine in magazines) {
            val response = magazineFactory.createResponse(magazine)
            magazinesResponseList.add(response)
        }
        return magazinesResponseList
    }

    override fun findById(theId: Int): Optional<Magazine> {
        return magazineRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<MagazineResponse> {
        val magazines: List<Magazine> = magazineRepository.findAll().stream()
            .filter { it.name.contains(theKeyword) }
            .toList()
        val magazinesResponseList = mutableListOf<MagazineResponse>()
        for (magazine in magazines) {
            val response = magazineFactory.createResponse(magazine)
            magazinesResponseList.add(response)
        }
        return magazinesResponseList
    }

    override fun save(theMagazine: MagazineRequest) {
        val magazine = magazineFactory.createMagazine(theMagazine)

        magazineRepository.save(magazine)
    }

    override fun deleteById(theId: Int) {
        magazineRepository.deleteById(theId)
    }
}
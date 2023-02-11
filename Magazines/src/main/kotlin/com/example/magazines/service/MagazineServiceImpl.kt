package com.example.magazines.service

import com.example.magazines.controller.MagazineRequest
import com.example.magazines.controller.MagazineResponse
import com.example.magazines.factories.MagazineFactory
import com.example.magazines.model.Magazine
import com.example.magazines.model.dto.MagazineDTO
import com.example.magazines.repository.MagazineRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@RequiredArgsConstructor
class MagazineServiceImpl(
    private val magazineRepository: MagazineRepository,
    private val magazineFactory: MagazineFactory
) : MagazineService {
    override fun findAllMagazines(): List<MagazineResponse> =
        magazineRepository.findAll()
            .map { magazineFactory.createResponse(it) }


    override fun findById(theId: Int): MagazineDTO =
        magazineRepository.findByIdOrNull(theId)?.toDTO()
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    override fun findAllByKeyword(theKeyword: String): List<MagazineResponse> =
        magazineRepository.findAll().stream()
            .filter { it.name.contains(theKeyword) }
            .map { magazineFactory.createResponse(it) }
            .toList()

    override fun save(theMagazine: MagazineRequest) {
        val magazine = magazineFactory.createMagazine(theMagazine)

        magazineRepository.save(magazine)
    }

    override fun deleteById(theId: Int): Unit =
        magazineRepository.deleteById(theId)
}
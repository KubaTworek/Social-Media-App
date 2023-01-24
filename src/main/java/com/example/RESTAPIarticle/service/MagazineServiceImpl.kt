package com.example.RESTAPIarticle.service

import com.example.RESTAPIarticle.entity.Magazine
import com.example.RESTAPIarticle.repository.MagazineRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class MagazineServiceImpl(private val magazineRepository: MagazineRepository) : MagazineService {

    override fun findById(theId: Int): Magazine? {
        return magazineRepository.findById(theId).orElse(null)
    }

    override fun save(theMagazine: Magazine) {
        theMagazine.id = 0
        magazineRepository.save(theMagazine)
    }

    override fun findByName(theName: String): Magazine? {
        return magazineRepository.findByName(theName)
    }
}
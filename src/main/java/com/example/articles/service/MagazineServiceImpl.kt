package com.example.articles.service

import com.example.articles.entity.Magazine
import com.example.articles.repository.MagazineRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class MagazineServiceImpl(private val magazineRepository: MagazineRepository) : MagazineService {

    override fun findById(theId: Int): Magazine? {
        return magazineRepository.findById(theId).orElse(null)
    }

    override fun save(theMagazine: Magazine) {
        magazineRepository.save(theMagazine)
    }

    override fun findByName(theName: String): Magazine? {
        return magazineRepository.findByName(theName).orElse(null)
    }
}
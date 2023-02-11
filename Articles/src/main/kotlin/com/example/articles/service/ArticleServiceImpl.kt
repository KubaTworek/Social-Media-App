package com.example.articles.service

import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.factories.ArticleFactory
import com.example.articles.model.dto.ArticleDTO
import com.example.articles.repository.ArticleRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@RequiredArgsConstructor
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val articleFactory: ArticleFactory,
) : ArticleService {
    override fun findAllOrderByDateDesc(): List<ArticleResponse> =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .map { articleFactory.createResponse(it) }

    override fun findById(theId: Int): ArticleResponse =
        articleFactory.createResponse(
            articleRepository.findByIdOrNull(theId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        )

    override fun findAllByAuthorId(authorId: Int): List<ArticleDTO> =
        articleRepository.findAllByAuthorIdOrderByDate(authorId)
            .stream()
            .map { it.toDTO() }
            .toList()

    override fun findAllByMagazineId(magazineId: Int): List<ArticleDTO> =
        articleRepository.findAllByMagazineIdOrderByDate(magazineId)
            .stream()
            .map { it.toDTO() }
            .toList()

    override fun findAllByKeyword(theKeyword: String): List<ArticleResponse> =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .filter { it.content.title.contains(theKeyword) || it.content.text.contains(theKeyword) }
            .map { articleFactory.createResponse(it) }


    override fun save(theArticle: ArticleRequest) {
        val article = articleFactory.createArticle(theArticle)
        articleRepository.save(article)
    }

    override fun deleteById(theId: Int) =
        articleRepository.deleteById(theId)
}
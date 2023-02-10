package com.example.articles.service

import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.factories.ArticleFactory
import com.example.articles.model.Article
import com.example.articles.repository.ArticleRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val articleFactory: ArticleFactory,
) : ArticleService {
    override fun findAllOrderByDateDesc(): List<ArticleResponse> {
        val articles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
        val articlesResponseList = mutableListOf<ArticleResponse>()
        for(article in articles){
            val response = articleFactory.createResponse(article)
            articlesResponseList.add(response)
        }
        return articlesResponseList
    }

    override fun findById(theId: Int): Optional<ArticleResponse> {
        return Optional.of(articleFactory.createResponse(articleRepository.findById(theId).get()))
    }

    override fun findAllByAuthorId(authorId: Int) = articleRepository.findAllByAuthorIdOrderByDate(authorId)

    override fun findAllByMagazineId(magazineId: Int) = articleRepository.findAllByMagazineIdOrderByDate(magazineId)

    override fun findAllByKeyword(theKeyword: String) : List<ArticleResponse> {
        val articles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .filter { it.content.title.contains(theKeyword) || it.content.text.contains(theKeyword) }
        val articlesResponseList = mutableListOf<ArticleResponse>()
        for(article in articles){
            val response = articleFactory.createResponse(article)
            articlesResponseList.add(response)
        }
        return articlesResponseList
    }


    override fun save(theArticle: ArticleRequest) {
        val article = articleFactory.createArticle(theArticle)

        articleRepository.save(article)
    }

    override fun deleteById(theId: Int) {
        articleRepository.deleteById(theId)
    }
}
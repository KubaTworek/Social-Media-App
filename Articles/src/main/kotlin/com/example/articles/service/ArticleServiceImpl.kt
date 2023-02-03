package com.example.articles.service

import com.example.articles.factories.ArticleFactory
import com.example.articles.model.Article
import com.example.articles.repository.ArticleRepository
import com.example.articles.controller.ArticleRequest
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class ArticleServiceImpl (
    private val articleRepository: ArticleRepository,
    private val articleFactory: ArticleFactory,
) : ArticleService {
    override fun findAllOrderByDateDesc(): List<Article> {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
    }

    override fun findById(theId: Int): Optional<Article> {
        return articleRepository.findById(theId)
    }


    override fun findAllByKeyword(theKeyword: String) =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .filter { it.content.title.contains(theKeyword) || it.content.text.contains(theKeyword) }

    override fun save(theArticle: ArticleRequest) {
        val article = articleFactory.createArticle(theArticle)

        articleRepository.save(article)
    }

    override fun deleteById(theId: Int) {
        articleRepository.deleteById(theId)
    }
}
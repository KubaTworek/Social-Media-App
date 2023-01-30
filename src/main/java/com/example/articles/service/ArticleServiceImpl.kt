package com.example.articles.service

import com.example.articles.controller.article.ArticleRequest
import com.example.articles.entity.ArticlePost
import com.example.articles.factories.ArticleFactory
import com.example.articles.repository.ArticleRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*
import kotlin.streams.toList

@Service
@RequiredArgsConstructor
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val articleFactory: ArticleFactory,
) : ArticleService {

    override fun findAllOrderByDateDesc(): List<ArticlePost> {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
    }

    override fun findById(theId: Int): Optional<ArticlePost> {
        return articleRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<ArticlePost> {
        val articles: List<ArticlePost> = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
        return articles.stream()
            .filter { article: ArticlePost ->
                article.content.title.contains(theKeyword) || article.content.text.contains(theKeyword)
            }
            .toList()
    }

    override fun save(theArticle: ArticleRequest) {
        val article = articleFactory.createArticle(theArticle)

        articleRepository.save(article)
    }

    override fun deleteById(theId: Int) {
        articleRepository.deleteById(theId)
    }
}

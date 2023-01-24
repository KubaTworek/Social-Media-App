package com.example.RESTAPIarticle.service

import com.example.RESTAPIarticle.entity.Article
import com.example.RESTAPIarticle.errors.ArticleNotFoundException
import com.example.RESTAPIarticle.repository.ArticleRepository
import com.example.RESTAPIarticle.repository.ContentRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val contentRepository: ContentRepository
) : ArticleService {

    override fun findAllOrderByDateDesc(): List<Article> {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
    }

    override fun findById(theId: Int): Article {
        return articleRepository.findById(theId)
            .orElseThrow { ArticleNotFoundException("Article id not found - $theId") }
    }

    override fun findAllByKeyword(theKeyword: String): List<Article> {
        val articles: List<Article> = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
        return articles.stream()
            .filter { article: Article ->
                contentRepository.findAllByTextContainsOrTitleContains(
                    theKeyword,
                    theKeyword
                )!!.contains(article.content)
            }
            .collect(Collectors.toList())
    }

    override fun save(theArticle: Article): Article {
        return articleRepository.save(theArticle)
    }

    override fun deleteById(theId: Int) {
        articleRepository.deleteById(theId)
    }
}

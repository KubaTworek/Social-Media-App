package com.example.articles.service

import com.example.articles.controller.ArticleRequest
import com.example.articles.entity.Article
import com.example.articles.errors.ArticleNotFoundException
import com.example.articles.factories.ArticleFactory
import com.example.articles.factories.AuthorFactory
import com.example.articles.factories.ContentFactory
import com.example.articles.factories.MagazineFactory
import com.example.articles.repository.ArticleRepository
import com.example.articles.repository.AuthorRepository
import com.example.articles.repository.ContentRepository
import com.example.articles.repository.MagazineRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val authorRepository: AuthorRepository,
    private val magazineRepository: MagazineRepository,
    private val contentRepository: ContentRepository,
    private val articleFactory: ArticleFactory,
    private val authorFactory: AuthorFactory,
    private val magazineFactory: MagazineFactory,
    private val contentFactory: ContentFactory,
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

    override fun save(theArticle: ArticleRequest) {
        val author =
            authorRepository.findByFirstNameAndLastName(theArticle.author_firstName, theArticle.author_lastName)
                .orElse(authorFactory.createAuthor(theArticle.author_firstName, theArticle.author_lastName))
        val magazine = magazineRepository.findByName(theArticle.magazine)
            .orElse(magazineFactory.createMagazine(theArticle.magazine))
        val content = contentFactory.createContent(theArticle.title, theArticle.text)
        val article = articleFactory.createArticle(author, magazine, content)

        articleRepository.save(article)
    }

    override fun deleteById(theId: Int) {
        articleRepository.deleteById(theId)
    }
}

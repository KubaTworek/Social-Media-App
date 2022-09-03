package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.ArticleDAO;
import com.example.RESTAPIarticle.dao.ContentDAO;
import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.entity.ArticleContent;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService{

    private final ArticleDAO articleDAO;
    private final ContentDAO contentDAO;
    private final Comparator<? super Article> ArticleComparator;

    public ArticleServiceImpl(ArticleDAO articleDAO, ContentDAO contentDAO, Comparator<? super Article> articleComparator) {
        this.articleDAO = articleDAO;
        this.contentDAO = contentDAO;
        this.ArticleComparator = articleComparator;
    }

    @Override
    public List<Article> findAllOrderByDateDesc() {
        return articleDAO.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    @Override
    public Article findById(int theId) {
        Optional<Article> result = articleDAO.findById(theId);

        Article theArticle = null;

        if (result.isPresent()) {
            theArticle = result.get();
        }

        return theArticle;
    }

    @Override
    public List<Article> findAllByKeyword(String theKeyword) {
        List<Article> articles = new ArrayList<>();
        for(ArticleContent tempContent : contentDAO.findAllByTextContainsOrTitleContains(theKeyword,theKeyword)){
            articles.addAll(articleDAO.findAllByContentOrderByDateDesc(tempContent));
        }
        articles.sort(Collections.reverseOrder(ArticleComparator));
        return articles;
    }

    @Override
    public void save(Article theArticle) {
        articleDAO.save(theArticle);
    }

    @Override
    public void deleteById(int theId) {
        articleDAO.deleteById(theId);
    }
}

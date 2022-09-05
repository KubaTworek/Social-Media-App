package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.ArticleDAO;
import com.example.RESTAPIarticle.dao.ContentDAO;
import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.entity.ArticleContent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleDAO articleDAO;
    private final ContentDAO contentDAO;
    private final Comparator<? super Article> ArticleComparator;

    @Override
    public List<Article> findAllOrderByDateDesc() {
        return articleDAO.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    @Override
    public Article findById(int theId) {
        return articleDAO.findById(theId).orElseThrow();
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
    public Article save(Article theArticle) {
        return articleDAO.save(theArticle);
    }

    @Override
    public void deleteById(int theId) {
        articleDAO.deleteById(theId);
    }
}

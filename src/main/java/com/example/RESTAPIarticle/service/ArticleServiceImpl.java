package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.ArticleDAO;
import com.example.RESTAPIarticle.dao.ContentDAO;
import com.example.RESTAPIarticle.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleDAO articleDAO;
    private final ContentDAO contentDAO;
    @Override
    public List<Article> findAllOrderByDateDesc() {
        return articleDAO.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    @Override
    public Article findById(int theId) {
        return articleDAO.findById(theId).orElse(null);
    }

    @Override
    public List<Article> findAllByKeyword(String theKeyword) {
        List<Article> articles = articleDAO.findAll(Sort.by(Sort.Direction.DESC, "date"));
        return articles.stream()
                .filter(article -> contentDAO.findAllByTextContainsOrTitleContains(theKeyword,theKeyword).contains(article.getContent()))
                .collect(Collectors.toList());
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

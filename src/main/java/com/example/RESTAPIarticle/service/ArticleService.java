package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> findAllOrderByDateDesc();
    Article findById(int theId);
    List<Article> findAllByKeyword(String theKeyword);
    Article save(Article theArticle);
    void deleteById(int theId);
}

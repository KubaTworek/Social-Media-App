package com.example.RESTAPIarticle.dao;

import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.entity.ArticleContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDAO extends JpaRepository<Article, Integer> {
    List<Article> findAllByContentOrderByDateDesc(ArticleContent theContent);
}

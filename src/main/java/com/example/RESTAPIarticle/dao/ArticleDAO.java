package com.example.RESTAPIarticle.dao;

import com.example.RESTAPIarticle.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDAO extends JpaRepository<Article, Integer> {
    //List<Article> findAllByKeyword(String keyword);
}

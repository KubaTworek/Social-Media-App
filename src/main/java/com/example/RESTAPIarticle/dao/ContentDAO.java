package com.example.RESTAPIarticle.dao;

import com.example.RESTAPIarticle.entity.ArticleContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentDAO extends JpaRepository<ArticleContent, Integer> {
    List<ArticleContent> findAllByTextContainsOrTitleContains(String keyword1, String keyword2);
}

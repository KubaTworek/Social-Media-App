package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.ArticleDAO;
import com.example.RESTAPIarticle.entity.Article;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService{

    private final ArticleDAO articleDAO;

    public ArticleServiceImpl(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
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

    /*@Override
    public List<Article> findAllByKeyword(String theKeyword) {
        return articleDAO.findAllByKeyword(theKeyword);
    }*/

    @Override
    public void save(Article theArticle) {
        articleDAO.save(theArticle);
    }

    @Override
    public void deleteById(int theId) {
        articleDAO.deleteById(theId);
    }
}

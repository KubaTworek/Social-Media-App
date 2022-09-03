package com.example.RESTAPIarticle.rest;

import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ArticleRestController {

    private final ArticleService articleService;

    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public List<Article> getArticlesOrderByDateDesc(){
        return articleService.findAllOrderByDateDesc();
    }

    @GetMapping("/article/{articleId}")
    public Article getArticleById(@PathVariable int articleId) throws Exception {
        if(articleService.findById(articleId) == null) throw new Exception("Article id not found - " + articleId);

        return articleService.findById(articleId);
    }

    /*@GetMapping("/articles/{keyword}")
    public List<Article> getArticlesByKeyword(@PathVariable String keyword){
        return articleService.findAllByKeyword(keyword);
    }*/

    @PostMapping("/article")
    public Article saveArticle(@RequestBody Article theArticle){
        theArticle.setId(0);
        articleService.save(theArticle);

        return theArticle;
    }

    @PostMapping("/article/update")
    public Article updateArticle(@RequestBody Article theArticle){
        articleService.save(theArticle);

        return theArticle;
    }

    @DeleteMapping("/article/{articleId}")
    public String deleteArticle(@PathVariable int articleId) throws Exception {
        if(articleService.findById(articleId) == null) throw new Exception("Article id not found - " + articleId);
        articleService.deleteById(articleId);

        return "Deleted article is - " + articleId;
    }
}

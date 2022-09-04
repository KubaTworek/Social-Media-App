package com.example.RESTAPIarticle.rest;

import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.errors.ArticleNotFoundException;
import com.example.RESTAPIarticle.errors.PropertyIsNullException;
import com.example.RESTAPIarticle.service.ArticleService;
import com.example.RESTAPIarticle.service.AuthorService;
import com.example.RESTAPIarticle.service.MagazineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ArticleRestController {

    private final AuthorService authorService;
    private final MagazineService magazineService;

    private final ArticleService articleService;

    public ArticleRestController(AuthorService authorService, MagazineService magazineService, ArticleService articleService) {
        this.authorService = authorService;
        this.magazineService = magazineService;
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public List<Article> getArticlesOrderByDateDesc(){
        return articleService.findAllOrderByDateDesc();
    }

    @GetMapping("/article/{articleId}")
    public Article getArticleById(@PathVariable int articleId) {
        if(articleService.findById(articleId) == null) throw new ArticleNotFoundException("Article id not found - " + articleId);

        return articleService.findById(articleId);
    }

    @GetMapping("/articles/{keyword}")
    public List<Article> getArticlesByKeyword(@PathVariable String keyword){
        return articleService.findAllByKeyword(keyword);
    }

    @PostMapping( "/articles")
    public Article saveArticle(@RequestBody Article theArticle){
        if(isNecessaryPropertiesNotNull(theArticle) && isNecessaryPropertiesExist(theArticle)){
            theArticle.setId(0);
            theArticle.getContent().setId(0);

            articleService.save(theArticle);
        }

        return theArticle;
    }

    @PutMapping("/articles")
    public Article updateArticle(@RequestBody Article theArticle){
        if(isNecessaryPropertiesNotNull(theArticle) && isNecessaryPropertiesExist(theArticle)){
            articleService.save(theArticle);
        }

        return theArticle;
    }

    @DeleteMapping("/article/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        if(articleService.findById(articleId) == null) throw new ArticleNotFoundException("Article id not found - " + articleId);
        articleService.deleteById(articleId);

        return "Deleted article is - " + articleId;
    }

    private boolean isNecessaryPropertiesExist(Article theArticle){
        if(!isAuthorExist(theArticle)) authorService.save(theArticle.getAuthor());
        if(!isMagazineExist(theArticle)) magazineService.save(theArticle.getMagazine());
        return true;
    }

    private boolean isNecessaryPropertiesNotNull(Article theArticle){
        if(theArticle.getContent() == null) throw new PropertyIsNullException("Content is null");
        if(theArticle.getContent().getTitle() == null) throw new PropertyIsNullException("Title is null");
        if(theArticle.getContent().getText() == null) throw new PropertyIsNullException("Text is null");
        if(theArticle.getDate() == null) throw new PropertyIsNullException("Date is null");
        if(theArticle.getAuthor() == null) throw new PropertyIsNullException("Author is null");
        if(theArticle.getMagazine() == null) throw new PropertyIsNullException("Magazine is null");
        return true;
    }

    private boolean isAuthorExist(Article theArticle){
        if(authorService.findById(theArticle.getAuthor().getId()) == null){
            if(theArticle.getAuthor().getFirstName() == null) throw new PropertyIsNullException("Author first name is null");
            if(theArticle.getAuthor().getLastName() == null) throw new PropertyIsNullException("Author last name is null");
            if(authorService.findByFirstNameAndLastName(theArticle.getAuthor().getFirstName(), theArticle.getAuthor().getLastName()) == null){
                return false;
            } else {
                theArticle.setAuthor(authorService.findByFirstNameAndLastName(theArticle.getAuthor().getFirstName(), theArticle.getAuthor().getLastName()));
            }
        } else {
            theArticle.setAuthor(authorService.findById(theArticle.getAuthor().getId()));
        }
        return true;
    }

    private boolean isMagazineExist(Article theArticle){
        if(magazineService.findById(theArticle.getMagazine().getId()) == null) {
            if(theArticle.getMagazine().getName() == null) throw new PropertyIsNullException("Magazine name is null");
            if(magazineService.findByName(theArticle.getMagazine().getName()) == null){
                return false;
            } else {
                theArticle.setMagazine(magazineService.findByName(theArticle.getMagazine().getName()));
            }
        } else {
            theArticle.setMagazine(magazineService.findById(theArticle.getMagazine().getId()));
        }
        return true;
    }
}

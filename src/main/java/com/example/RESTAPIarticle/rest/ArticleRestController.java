package com.example.RESTAPIarticle.rest;

import com.example.RESTAPIarticle.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import com.example.RESTAPIarticle.errors.PropertyIsNullException;
import com.example.RESTAPIarticle.service.ArticleService;
import com.example.RESTAPIarticle.service.AuthorService;
import com.example.RESTAPIarticle.service.MagazineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ArticleRestController {

    public static final int EMPTY_ID = 0;
    private final AuthorService authorService;
    private final MagazineService magazineService;
    private final ArticleService articleService;

    @GetMapping("/articles")
    public CollectionModel<EntityModel<Article>> getArticlesOrderByDateDesc(){
        List<EntityModel<Article>> articles = articleService.findAllOrderByDateDesc().stream()
                .map(article -> EntityModel.of(article,
                        linkTo(methodOn(ArticleRestController.class).getArticleById(article.getId())).withSelfRel(),
                        linkTo(methodOn(ArticleRestController.class).getArticlesOrderByDateDesc()).withRel("articles")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(articles,
                linkTo(methodOn(ArticleRestController.class).getArticlesOrderByDateDesc()).withSelfRel()
        );
    }

    @GetMapping("/article/{articleId}")
    public EntityModel<Article> getArticleById(@PathVariable int articleId) {
        return EntityModel.of(articleService.findById(articleId),
                linkTo(methodOn(ArticleRestController.class).getArticleById(articleId)).withSelfRel(),
                linkTo(methodOn(ArticleRestController.class).getArticlesOrderByDateDesc()).withRel("articles")
        );
    }

    @GetMapping("/articles/{keyword}")
    public CollectionModel<EntityModel<Article>> getArticlesByKeyword(@PathVariable String keyword){
        List<EntityModel<Article>> articles = articleService.findAllByKeyword(keyword).stream()
                .map(article -> EntityModel.of(article,
                        linkTo(methodOn(ArticleRestController.class).getArticleById(article.getId())).withSelfRel(),
                        linkTo(methodOn(ArticleRestController.class).getArticlesOrderByDateDesc()).withRel("articles")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(articles,
                linkTo(methodOn(ArticleRestController.class).getArticlesByKeyword(keyword)).withSelfRel()
        );
    }

    @PostMapping( "/articles")
    public ResponseEntity<EntityModel<Article>> saveArticle(@RequestBody Article theArticle){
        if(isNecessaryPropertiesNotNull(theArticle) && isNecessaryPropertiesExist(theArticle)){
            Article article = articleService.save(new Article(
                    EMPTY_ID,
                    theArticle.getContent(),
                    theArticle.getDate(),
                    theArticle.getMagazine(),
                    theArticle.getAuthor()
            ));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(EntityModel.of(article,
                            linkTo(methodOn(ArticleRestController.class).saveArticle(theArticle)).withSelfRel(),
                            linkTo(methodOn(ArticleRestController.class).getArticlesOrderByDateDesc()).withRel("articles")
                    ));
        }
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<Object> updateArticle(@PathVariable int articleId, @RequestBody Article theArticle){
        if(isNecessaryPropertiesNotNull(theArticle) && isNecessaryPropertiesExist(theArticle)){
            articleService.save(new Article(
                    articleId,
                    theArticle.getContent(),
                    theArticle.getDate(),
                    theArticle.getMagazine(),
                    theArticle.getAuthor()
            ));
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/article/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
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

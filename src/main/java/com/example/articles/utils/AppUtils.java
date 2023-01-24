package com.example.articles.utils;

import com.example.articles.entity.Article;
import com.example.articles.errors.PropertyIsNullException;

public class AppUtils {
    public static boolean isArticlePropertiesNotNull(Article theArticle){
        if(theArticle.getContent() == null) throw new PropertyIsNullException("Content is null");
        if(theArticle.getContent().getTitle() == null) throw new PropertyIsNullException("Title is null");
        if(theArticle.getContent().getText() == null) throw new PropertyIsNullException("Text is null");
        if(theArticle.getDate() == null) throw new PropertyIsNullException("Date is null");
        if(theArticle.getAuthor() == null) throw new PropertyIsNullException("Author is null");
        if(theArticle.getMagazine() == null) throw new PropertyIsNullException("Magazine is null");
        return true;
    }

}

package com.example.articles.model;

import com.example.articles.entity.Article;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

@Component
public class ArticleComparator implements Comparator<Article>{
    @Override
    public int compare(Article article, Article t1) {
        Date firstDate = stringToDateFormatter(article.getDate());
        Date secondDate = stringToDateFormatter(t1.getDate());
        return Long.compare(firstDate.getTime(),secondDate.getTime());
    }

    private Date stringToDateFormatter(String dateString) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

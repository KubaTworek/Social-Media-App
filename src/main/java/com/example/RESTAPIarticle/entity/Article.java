package com.example.RESTAPIarticle.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name="Article")
@Component
public class Article {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne(cascade = {CascadeType.ALL, CascadeType.ALL})
    @JoinColumn(name = "content_id")
    private ArticleContent content;

    @Column(name="date")
    private String date;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="magazine_id")
    private Magazine magazine;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="author_id")
    private Author author;

    @Column(name="timestamp")
    private long timestamp;

    public Article() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArticleContent getContent() {
        return content;
    }

    public void setContent(ArticleContent content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", content=" + content +
                ", date='" + date + '\'' +
                ", magazine=" + magazine +
                ", author=" + author +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

package com.example.application.database.models;

import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "Article")
public class ArticleModel {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private UserModel author;
    @Column(nullable = false)
    private ArticleCategoryEnum category;
    @Column(nullable = false)
    private ArticleTagEnum tag;
    @Column(nullable = false)
    private Timestamp insertDate;
    @Column(nullable = false)
    private Timestamp updateDate;


    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserModel getAuthor() {
        return this.author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public ArticleCategoryEnum getCategory() {
        return this.category;
    }

    public void setCategory(ArticleCategoryEnum category) {
        this.category = category;
    }

    public ArticleTagEnum getTag() {
        return this.tag;
    }

    public void setTag(ArticleTagEnum tag) {
        this.tag = tag;
    }

    public Timestamp getInsertDate() {
        return this.insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public void update(String title, String content, UserModel author, ArticleCategoryEnum category, ArticleTagEnum tag) {
        if (title != null) this.setTitle(title);
        if (content != null) this.setContent(content);
        if (author != null) this.setAuthor(author);
        if (category != null) this.setCategory(category);
        if (tag != null) this.setTag(tag);
        this.setUpdateDate(new Timestamp(System.currentTimeMillis()));
    }
}

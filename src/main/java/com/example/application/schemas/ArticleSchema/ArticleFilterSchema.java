package com.example.application.schemas.ArticleSchema;

import com.example.application.database.models.UserModel;
import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleFilterSchema {
    private final String title;
    private final UserModel author;
    private final List<ArticleCategoryEnum> category;
    private final List<ArticleTagEnum> tag;

    public ArticleFilterSchema(final String title, final UserModel author, final ArticleCategoryEnum category, final ArticleTagEnum tag) {
        this.title = Objects.requireNonNullElse(title, "");
        this.author = author;
        this.category = new ArrayList<>();
        this.tag = new ArrayList<>();
        if (category != null) {
            this.category.add(category);
        } else {
            this.category.addAll(List.of(ArticleCategoryEnum.values()));
        }
        if (tag != null) {
            this.tag.add(tag);
        } else {
            this.tag.addAll(List.of(ArticleTagEnum.values()));
        }
    }

    public String getTitle() {
        return this.title;
    }

    public UserModel getAuthor() {
        return this.author;
    }

    public List<ArticleCategoryEnum> getCategory() {
        return this.category;
    }

    public List<ArticleTagEnum> getTag() {
        return this.tag;
    }
}

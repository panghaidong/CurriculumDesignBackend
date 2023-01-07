package com.example.application.utils;

import com.example.application.database.models.ArticleModel;
import com.example.application.database.models.UserModel;
import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;
import com.example.application.enums.UserEnum.UserPowerEnum;
import com.example.application.schemas.ArticleSchema.ArticleOutSchema;
import com.example.application.schemas.UserSchema.UserOutSchema;

import java.sql.Timestamp;
import java.util.UUID;

public class Model2Schema {
    public static UserOutSchema user2UserOutSchema(UserModel user) {
        final UUID id = user.getId();
        final String name = user.getName();
        final String email = user.getEmail();
        final UserPowerEnum power = user.getPower();
        return new UserOutSchema(id, name, email, power);
    }

    public static ArticleOutSchema article2ArticleOutSchema(ArticleModel article) {
        final UUID id = article.getId();
        final String title = article.getTitle();
        final String content = article.getContent();
        final UserModel author = article.getAuthor();
        final ArticleCategoryEnum category = article.getCategory();
        final ArticleTagEnum tag = article.getTag();
        final Timestamp insertDate = article.getInsertDate();
        final Timestamp updateDate = article.getUpdateDate();
        return new ArticleOutSchema(id, title, content, user2UserOutSchema(author), category, tag, insertDate, updateDate);
    }
}

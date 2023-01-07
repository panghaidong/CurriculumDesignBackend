package com.example.application.schemas.ArticleSchema;

import com.example.application.database.models.UserModel;
import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;

import java.sql.Timestamp;
import java.util.UUID;

public record ArticleOutSchema(UUID id, String title, String content, UserModel author, ArticleCategoryEnum category,
                               ArticleTagEnum tags, Timestamp insertDate, Timestamp updateDate) {
}

package com.example.application.schemas.ArticleSchema;

import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;
import com.example.application.schemas.UserSchema.UserOutSchema;

import java.sql.Timestamp;
import java.util.UUID;

public record ArticleOutSchema(UUID id, String title, String content, UserOutSchema author, ArticleCategoryEnum category,
                               ArticleTagEnum tags, Timestamp insertDate, Timestamp updateDate) {
}

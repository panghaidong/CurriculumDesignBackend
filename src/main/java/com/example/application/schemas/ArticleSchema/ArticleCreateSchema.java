package com.example.application.schemas.ArticleSchema;

import com.example.application.database.models.UserModel;
import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;

public record ArticleCreateSchema(String title, String content, UserModel author, ArticleCategoryEnum category,
                                  ArticleTagEnum tags) {
}
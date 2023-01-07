package com.example.application.database.repository;

import com.example.application.database.models.ArticleModel;
import com.example.application.database.models.UserModel;
import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleModel, String> {
    List<ArticleModel> findByTitleContainsAndAuthorAndCategoryInAndTagIn(String title, UserModel author, Collection<ArticleCategoryEnum> category, Collection<ArticleTagEnum> tag);

}

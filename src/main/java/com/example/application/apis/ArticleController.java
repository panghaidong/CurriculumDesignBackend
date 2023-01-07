package com.example.application.apis;


import com.example.application.database.models.ArticleModel;
import com.example.application.database.models.UserModel;
import com.example.application.database.repository.ArticleRepository;
import com.example.application.database.repository.UserRepository;
import com.example.application.enums.ArticleEnum.ArticleCategoryEnum;
import com.example.application.enums.ArticleEnum.ArticleTagEnum;
import com.example.application.schemas.ArticleSchema.ArticleCreateSchema;
import com.example.application.schemas.ArticleSchema.ArticleFilterSchema;
import com.example.application.schemas.ArticleSchema.ArticleOutSchema;
import com.example.application.utils.Model2Schema;
import com.example.application.utils.Verify;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/articles", produces = "application/json")
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    // Inject the ArticleRepository and UserRepository
    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    // Get all articles after filter
    @Operation(summary = "Get articles")
    @Tag(name = "Article")
    @GetMapping
    public List<ArticleOutSchema> getArticles(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String author,
                                              @RequestParam(required = false) ArticleCategoryEnum category,
                                              @RequestParam(required = false) ArticleTagEnum tag) {
        // Get User instance by author id
        UserModel user;
        try {
            user = this.userRepository.findById(author).orElse(null);
        } catch (Exception e) {
            user = null;
        }
        // TODO: Add pagination
        // TODO: Add sort
        ArticleFilterSchema articleFilterSchema = new ArticleFilterSchema(title, user, category, tag);
        // Get all articles after filter by filter schema
        List<ArticleModel> articles = articleRepository.findByTitleContainsAndAuthorAndCategoryInAndTagIn(
                articleFilterSchema.getTitle(),
                articleFilterSchema.getAuthor(),
                articleFilterSchema.getCategory(),
                articleFilterSchema.getTag());
        // Convert ArticleModel to ArticleOutSchema
        return articles.stream().map(Model2Schema::article2ArticleOutSchema).toList();
    }

    // Create a new article
    @Operation(summary = "Create article")
    @Tag(name = "Article")
    @PutMapping
    public ArticleOutSchema createArticle(@RequestBody ArticleCreateSchema form,
                                          HttpServletResponse response,
                                          HttpSession session) {
        // Get user id from session
        ArticleModel article = new ArticleModel();
        UUID userId = (UUID) session.getAttribute("id");
        UserModel user;
        if (userId == null) {
            // not login
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            // Get user instance by user id
            user = userRepository.findById(userId.toString()).orElse(null);
            // Set article id
            article.setAuthor(user);
        }
        ArticleOutSchema result = null;
        // Set article record
        article.update(form.title(), form.content(), user, form.category(), form.tags());
        try {
            // Save article to database
            final ArticleModel saveResult = articleRepository.save(article);
            // Convert ArticleModel to ArticleOutSchema
            result = Model2Schema.article2ArticleOutSchema(saveResult);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }

    // Get article by id
    @Operation(summary = "Get article by id")
    @Tag(name = "Article")
    @GetMapping("/{articleId}")
    public ArticleOutSchema getArticle(@PathVariable String articleId,
                                       HttpServletResponse response) {
        // Get article instance by article id
        ArticleModel article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
            // Article not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        // Convert ArticleModel to ArticleOutSchema
        return Model2Schema.article2ArticleOutSchema(article);
    }

    // Update article by id
    @Operation(summary = "Update article by id")
    @Tag(name = "Article")
    @PatchMapping("/{articleId}")
    public ArticleOutSchema updateArticle(@PathVariable String articleId,
                                          @RequestBody ArticleCreateSchema form,
                                          HttpServletResponse response,
                                          HttpSession session) {
        ArticleModel article = articleRepository.findById(articleId).orElse(null);
        ArticleOutSchema result = null;
        if (article == null) {
            // Article not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        // Get author id from article instance
        UUID authorId = article.getAuthor().getId();
        if (!Verify.userPowerVerify(session, authorId)) {
            // User not login or permission denied
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        // Update article record
        article.update(form.title(), form.content(), article.getAuthor(), form.category(), form.tags());
        try {
            // Save article to database
            final ArticleModel saveResult = articleRepository.save(article);
            // Convert ArticleModel to ArticleOutSchema
            result = Model2Schema.article2ArticleOutSchema(saveResult);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }

    // Delete article by id
    @Operation(summary = "Delete article by id")
    @Tag(name = "Article")
    @DeleteMapping("/{articleId}")
    public ArticleOutSchema deleteArticle(@PathVariable String articleId,
                                          HttpServletResponse response,
                                          HttpSession session) {
        if (!Verify.userPowerVerify(session, UUID.fromString(articleId))) {
            // User not login or permission denied
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            ArticleModel article = articleRepository.findById(articleId).orElse(null);
            if (article == null) {
                // Article not found
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                articleRepository.delete(article);
                return Model2Schema.article2ArticleOutSchema(article);
            }
        }
    }

}
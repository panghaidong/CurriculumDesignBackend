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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
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
    @GetMapping("/")
    public List<ArticleOutSchema> getArticles(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String author,
                                              @RequestParam(required = false) ArticleCategoryEnum category,
                                              @RequestParam(required = false) ArticleTagEnum tag) {
        // Get User instance by author id
        UserModel user = this.userRepository.findById(author).orElse(null);
        // TODO: Add pagination
        // TODO: Add sort
        ArticleFilterSchema articleFilterSchema = new ArticleFilterSchema(title, user, category, tag);
        List<ArticleModel> articles = articleRepository.findByTitleContainsAndAuthorAndCategoryInAndTagIn(
                articleFilterSchema.getTitle(),
                articleFilterSchema.getAuthor(),
                articleFilterSchema.getCategory(),
                articleFilterSchema.getTag());
        return articles.stream().map(Model2Schema::article2ArticleOutSchema).toList();
    }

    @PutMapping("/")
    public ArticleOutSchema createArticle(@RequestBody ArticleCreateSchema form,
                                          HttpServletResponse response,
                                          HttpSession session) {

        ArticleModel article = new ArticleModel();
        UUID userId = (UUID) session.getAttribute("id");
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            UserModel user = userRepository.findById(userId.toString()).orElse(null);
            article.setAuthor(user);
        }
        ArticleOutSchema result = null;
        article.update(form.title(), form.content(), form.author(), form.category(), form.tags());

        try {
            final ArticleModel saveResult = articleRepository.save(article);
            result = Model2Schema.article2ArticleOutSchema(saveResult);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }

    @GetMapping("/{articleId}")
    public ArticleOutSchema getArticle(@PathVariable String articleId,
                                       HttpServletResponse response) {
        ArticleModel article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return Model2Schema.article2ArticleOutSchema(article);

    }

    @PatchMapping("/{articleId}")
    public ArticleOutSchema updateArticle(@PathVariable String articleId,
                                          @RequestBody ArticleCreateSchema form,
                                          HttpServletResponse response,
                                          HttpSession session) {
        // todo: complete this function
        ArticleModel article = articleRepository.findById(articleId).orElse(null);
        return null;
    }

}
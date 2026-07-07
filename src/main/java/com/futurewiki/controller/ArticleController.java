package com.futurewiki.controller;

import com.futurewiki.dto.CreateArticleRequest;
import com.futurewiki.dto.request.UpdateArticleRequest;
import com.futurewiki.dto.response.ArticleResponse;
import com.futurewiki.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createArticle(
            @Valid @RequestBody CreateArticleRequest request) {

        articleService.createArticle(request);
    }

    @GetMapping
    public List<ArticleResponse> getArticles(
            @RequestParam(required = false) String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return articleService.getMyArticles();
        }

        return articleService.searchArticles(keyword);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticle(
            @PathVariable Long id) {

        return articleService.getArticleById(id);
    }

    @PutMapping("/{id}")
    public ArticleResponse updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateArticleRequest request) {

        return articleService.updateArticle(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(
            @PathVariable Long id) {

        articleService.deleteArticle(id);
    }

}
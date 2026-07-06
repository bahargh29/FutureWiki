package com.futurewiki.service;

import com.futurewiki.dto.CreateArticleRequest;
import com.futurewiki.dto.request.UpdateArticleRequest;
import com.futurewiki.dto.response.ArticleResponse;
import com.futurewiki.entity.Article;
import com.futurewiki.entity.User;
import com.futurewiki.repository.ArticleRepository;
import com.futurewiki.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            UserRepository userRepository) {

        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void createArticle(CreateArticleRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article = new Article();

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setOwner(owner);

        articleRepository.save(article);
    }

    public List<ArticleResponse> getMyArticles(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Article> articles = articleRepository.findByOwner(owner);

        return articles.stream()
                .map(article -> {
                    ArticleResponse response = new ArticleResponse();

                    response.setId(article.getId());
                    response.setTitle(article.getTitle());
                    response.setContent(article.getContent());
                    response.setCreatedAt(article.getCreatedAt());
                    response.setUpdatedAt(article.getUpdatedAt());
                    return response;
                })
                .toList();
    }

    public ArticleResponse getArticleById(Long id){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article =
                articleRepository
                        .findByIdAndOwner(id, owner)
                        .orElseThrow(() -> new RuntimeException("Article not found"));

        ArticleResponse response = new ArticleResponse();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());

        return response;
    }

    public ArticleResponse updateArticle(
            Long id,
            UpdateArticleRequest request){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article =
                articleRepository
                        .findByIdAndOwner(id, owner)
                        .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        articleRepository.save(article);

        ArticleResponse response = new ArticleResponse();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());

        return response;
    }

    public void deleteArticle(Long id){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article =
                articleRepository
                        .findByIdAndOwner(id, owner)
                        .orElseThrow(() -> new RuntimeException("Article not found"));

        articleRepository.delete(article);
    }
}
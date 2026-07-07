package com.futurewiki.service;

import com.futurewiki.dto.CreateArticleRequest;
import com.futurewiki.dto.request.UpdateArticleRequest;
import com.futurewiki.dto.response.ArticleResponse;
import com.futurewiki.entity.Article;
import com.futurewiki.entity.User;
import com.futurewiki.exception.ArticleNotFoundException;
import com.futurewiki.mapper.ArticleMapper;
import com.futurewiki.repository.ArticleRepository;
import com.futurewiki.repository.UserRepository;
import com.futurewiki.service.security.CurrentUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final ArticleMapper articleMapper;

    public ArticleService(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService,
            ArticleMapper articleMapper) {

        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.articleMapper = articleMapper;
    }

    public void createArticle(CreateArticleRequest request) {

        User owner = currentUserService.getCurrentUser();

        Article article = new Article();

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setOwner(owner);

        articleRepository.save(article);
    }

    public Page<ArticleResponse> getMyArticles(Pageable pageable){

        User owner = currentUserService.getCurrentUser();

        Page<Article> articles =
                articleRepository.findByOwner(owner, pageable);

        return articles.map(articleMapper::toResponse);
    }

    public ArticleResponse getArticleById(Long id){

        User owner = currentUserService.getCurrentUser();

        Article article =
                articleRepository
                        .findByIdAndOwner(id, owner)
                        .orElseThrow(ArticleNotFoundException::new);

        return articleMapper.toResponse(article);
    }

    public ArticleResponse updateArticle(
            Long id,
            UpdateArticleRequest request){

        User owner = currentUserService.getCurrentUser();

        Article article =
                articleRepository
                        .findByIdAndOwner(id, owner)
                        .orElseThrow(ArticleNotFoundException::new);

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        articleRepository.save(article);

        return articleMapper.toResponse(article);
    }

    public void deleteArticle(Long id){

        User owner = currentUserService.getCurrentUser();

        Article article =
                articleRepository
                        .findByIdAndOwner(id, owner)
                        .orElseThrow(ArticleNotFoundException::new);

        articleRepository.delete(article);
    }

    public Page<ArticleResponse> searchArticles(String keyword, Pageable pageable) {

        User owner = currentUserService.getCurrentUser();

        Page<Article> articles =
                articleRepository.searchByOwnerAndKeyword(owner, keyword, pageable);

        return articles.map(articleMapper::toResponse);
    }
}
package com.futurewiki.service;

import com.futurewiki.dto.CreateArticleRequest;
import com.futurewiki.dto.request.UpdateArticleRequest;
import com.futurewiki.dto.response.ArticleResponse;
import com.futurewiki.entity.Article;
import com.futurewiki.entity.User;
import com.futurewiki.exception.ArticleNotFoundException;
import com.futurewiki.mapper.ArticleMapper;
import com.futurewiki.repository.ArticleRepository;
import com.futurewiki.service.security.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleService articleService;

    @Captor
    private ArgumentCaptor<Article> articleCaptor;

    @Test
    void shouldReturnCurrentUserArticles() {

        // Arrange

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("Bahar");

        Article article = new Article();
        article.setId(1L);
        article.setTitle("Spring Boot");
        article.setOwner(owner);

        ArticleResponse response = new ArticleResponse();
        response.setId(1L);
        response.setTitle("Spring Boot");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Article> articlePage =
                new PageImpl<>(List.of(article));

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.findByOwner(owner, pageable))
                .thenReturn(articlePage);

        when(articleMapper.toResponse(article))
                .thenReturn(response);

        // Act

        Page<ArticleResponse> result =
                articleService.getMyArticles(pageable);

        // Assert

        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Spring Boot",
                result.getContent().getFirst().getTitle()
        );

        verify(currentUserService).getCurrentUser();

        verify(articleRepository)
                .findByOwner(owner, pageable);

        verify(articleMapper)
                .toResponse(article);
    }

    @Test
    void shouldCreateArticleSuccessfully() {

        // Arrange

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("Bahar");

        CreateArticleRequest request = new CreateArticleRequest();
        request.setTitle("Spring Boot");
        request.setContent("Spring Boot Content");

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        // Act

        articleService.createArticle(request);

        // Assert

        verify(articleRepository).save(articleCaptor.capture());

        Article savedArticle = articleCaptor.getValue();

        assertEquals(request.getTitle(), savedArticle.getTitle());

        assertEquals(request.getContent(), savedArticle.getContent());

        assertEquals(owner, savedArticle.getOwner());

        assertNotNull(savedArticle.getCreatedAt());

        assertNotNull(savedArticle.getUpdatedAt());
    }

    @Test
    void shouldUpdateArticleSuccessfully(){

        // Arrange

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("Bahar");

        Article article = new Article();

        article.setId(1L);
        article.setTitle("Old Title");
        article.setContent("Old Content");
        article.setOwner(owner);

        UpdateArticleRequest request = new UpdateArticleRequest();

        request.setTitle("New Title");
        request.setContent("New Content");

        ArticleResponse response = new ArticleResponse();

        response.setId(1L);
        response.setTitle("New Title");
        response.setContent("New Content");

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.findByIdAndOwner(1L, owner))
                .thenReturn(Optional.of(article));

        when(articleMapper.toResponse(article))
                .thenReturn(response);

        // Act

        ArticleResponse result =
                articleService.updateArticle(1L, request);

        // Assert

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getTitle(), result.getTitle());
        assertEquals(response.getContent(), result.getContent());

        verify(articleRepository)
                .save(articleCaptor.capture());

        Article savedArticle =
                articleCaptor.getValue();

        assertEquals(
                request.getTitle(),
                savedArticle.getTitle()
        );
        assertEquals(
                request.getContent(),
                savedArticle.getContent()
        );
        assertEquals(
                owner,
                savedArticle.getOwner()
        );
        assertNotNull(
                savedArticle.getUpdatedAt()
        );

        verify(articleMapper)
                .toResponse(article);

        assertEquals(
                response,
                result
        );
    }
    @Test
    void shouldDeleteArticleSuccessfully(){

        // Arrange

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("Bahar");

        Article article = new Article();
        article.setId(1L);
        article.setTitle("Spring Boot");
        article.setOwner(owner);

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.findByIdAndOwner(1L, owner))
                .thenReturn(Optional.of(article));

        // Act

        articleService.deleteArticle(1L);

        // Assert

        verify(currentUserService).getCurrentUser();

        verify(articleRepository)
                .findByIdAndOwner(1L, owner);

        verify(articleRepository)
                .delete(article);
    }

    @Test
    void shouldThrowArticleNotFoundExceptionWhenDeletingArticle() {

        // Arrange
        User owner = new User();
        owner.setId(1L);

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.findByIdAndOwner(1L, owner))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ArticleNotFoundException.class,
                () -> articleService.deleteArticle(1L)
        );

        verify(articleRepository, never())
                .delete(any());
    }

    @Test
    void shouldReturnMatchingArticlesForCurrentUser(){

        // Arrange

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("Bahar");

        Article article = new Article();
        article.setId(1L);
        article.setTitle("Spring Boot");
        article.setContent("Spring Boot Tutorial");
        article.setOwner(owner);

        ArticleResponse response = new ArticleResponse();
        response.setId(1L);
        response.setTitle("Spring Boot");
        response.setContent("Spring Boot Tutorial");

        String keyword = "spring";

        Pageable pageable = PageRequest.of(0, 10);

        Page<Article> articlePage =
                new PageImpl<>(List.of(article));

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.searchByOwnerAndKeyword(owner, keyword, pageable))
                .thenReturn(articlePage);

        when(articleMapper.toResponse(article))
                .thenReturn(response);

        // Act

        Page<ArticleResponse> result =
                articleService.searchArticles(keyword, pageable);

        // Assert

        verify(currentUserService).getCurrentUser();

        verify(articleRepository)
                .searchByOwnerAndKeyword(owner, keyword, pageable);

        verify(articleMapper).toResponse(article);

        assertEquals(1, result.getTotalElements());

        ArticleResponse returned =
                result.getContent().getFirst();

        assertEquals(response.getId(), returned.getId());
        assertEquals(response.getTitle(), returned.getTitle());
        assertEquals(response.getContent(), returned.getContent());
    }

    @Test
    void shouldReturnArticleById(){

        // Arrange

        Long articleId = 1L;

        User owner = new User();
        owner.setId(1L);

        Article article = new Article();
        article.setId(articleId);
        article.setOwner(owner);
        article.setTitle("Spring");
        article.setContent("Content");

        ArticleResponse response = new ArticleResponse();
        response.setId(articleId);
        response.setTitle("Spring");
        response.setContent("Content");

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.findByIdAndOwner(articleId, owner))
                .thenReturn(Optional.of(article));

        when(articleMapper.toResponse(article))
                .thenReturn(response);

        // Act

        ArticleResponse result =articleService.getArticleById(articleId);

        // Assert

        verify(currentUserService).getCurrentUser();

        verify(articleRepository).findByIdAndOwner(articleId, owner);

        verify(articleMapper).toResponse(article);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getTitle(), result.getTitle());
        assertEquals(response.getContent(), result.getContent());
    }

    @Test
    void shouldThrowArticleNotFoundExceptionWhenGettingArticleById(){

        // Arrange

        Long articleId = 1L;

        User owner = new User();
        owner.setId(1L);

        when(currentUserService.getCurrentUser())
                .thenReturn(owner);

        when(articleRepository.findByIdAndOwner(articleId, owner))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ArticleNotFoundException.class,
                () -> articleService.getArticleById(articleId)
        );

        verify(articleMapper, never())
                .toResponse(any());

        verify(articleRepository)
                .findByIdAndOwner(articleId, owner);
    }
}
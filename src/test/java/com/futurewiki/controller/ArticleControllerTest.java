package com.futurewiki.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futurewiki.dto.CreateArticleRequest;
import com.futurewiki.dto.request.UpdateArticleRequest;
import com.futurewiki.dto.response.ArticleResponse;
import com.futurewiki.security.jwt.JwtAuthenticationFilter;
import com.futurewiki.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldReturnArticleById() throws Exception {

        // Arrange

        ArticleResponse response = new ArticleResponse();

        response.setId(1L);
        response.setTitle("Spring Boot");
        response.setContent("Spring Boot Tutorial");


        when(articleService.getArticleById(1L))
                .thenReturn(response);


        // Act + Assert

        mockMvc.perform(
                        get("/api/articles/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot"))
                .andExpect(jsonPath("$.content").value("Spring Boot Tutorial"));
    }

    @Test
    void shouldCreateArticleSuccessfully() throws Exception {

        // Arrange

        CreateArticleRequest request = new CreateArticleRequest();
        request.setTitle("Spring Boot");
        request.setContent("Content");

        // Act + Assert

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(articleService).createArticle(any(CreateArticleRequest.class));
    }

    @Test
    void shouldReturnCurrentUserArticles() throws Exception{

        // Arrange

        ArticleResponse response = new ArticleResponse();
        response.setId(1L);
        response.setTitle("Spring Boot");

        Page<ArticleResponse> page =
                new PageImpl<>(List.of(response));

        when(articleService.getMyArticles(any(Pageable.class)))
                .thenReturn(page);

        // Act + Assert

        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Spring Boot"));

        verify(articleService)
                .getMyArticles(any(Pageable.class));
    }

    @Test
    void shouldReturnFilteredArticles() throws Exception{

        // Arrange

        ArticleResponse response = new ArticleResponse();
        response.setId(1L);
        response.setTitle("Spring Boot");

        Page<ArticleResponse> page =
                new PageImpl<>(List.of(response));

        when(articleService.searchArticles(eq("spring"), any(Pageable.class)))
                .thenReturn(page);

        // Act + Assert

        mockMvc.perform(get("/api/articles?keyword=spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Spring Boot"));

        verify(articleService)
                .searchArticles(eq("spring"), any(Pageable.class));
    }

    @Test
    void shouldUpdateArticleSuccessfully() throws Exception {

        // Arrange
        Long articleId = 1L;

        UpdateArticleRequest request = new UpdateArticleRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        ArticleResponse response = new ArticleResponse();
        response.setId(articleId);
        response.setTitle("Updated Title");
        response.setContent("Updated Content");

        when(articleService.updateArticle(eq(articleId), any(UpdateArticleRequest.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(put("/api/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("bahar")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(articleId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        verify(articleService).updateArticle(eq(articleId), any(UpdateArticleRequest.class));
    }

    @Test
    void shouldDeleteArticleSuccessfully() throws Exception {

        // Arrange
        Long articleId = 1L;

        doNothing().when(articleService).deleteArticle(articleId);

        // Act + Assert
        mockMvc.perform(delete("/api/articles/{id}", articleId)
                        .with(user("bahar")))
                .andExpect(status().isNoContent());

        verify(articleService).deleteArticle(articleId);
    }
}

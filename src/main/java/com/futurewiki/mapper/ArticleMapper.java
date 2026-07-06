package com.futurewiki.mapper;

import com.futurewiki.dto.response.ArticleResponse;
import com.futurewiki.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {

        ArticleResponse response = new ArticleResponse();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());

        return response;
    }

}
package com.futurewiki.repository;

import com.futurewiki.entity.Article;
import com.futurewiki.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwner(User owner);

    Optional<Article> findByIdAndOwner(Long id, User owner);

}
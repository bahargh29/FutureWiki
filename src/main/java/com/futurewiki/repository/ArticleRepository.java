package com.futurewiki.repository;

import com.futurewiki.entity.Article;
import com.futurewiki.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByOwner(User owner);

    Optional<Article> findByIdAndOwner(Long id, User owner);

    @Query("""
    SELECT a
    FROM Article a
    WHERE a.owner = :owner
      AND (
           LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    """)
    Page<Article> searchByOwnerAndKeyword(
            @Param("owner") User owner,
            @Param("keyword") String keyword,
            Pageable pageable);

    Page<Article> findByOwner(
            User owner,
            Pageable pageable
    );

}
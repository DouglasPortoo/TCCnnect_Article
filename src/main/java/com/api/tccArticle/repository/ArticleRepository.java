package com.api.tccArticle.repository;

import com.api.tccArticle.domain.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article,String> {
    List<Article> findByCdAuthor(String cdAuthor);
    List<Article> findByTitleIgnoreCaseContaining(String title);
    List<Article> findByStatusIgnoreCase(String status);
    List<Article> findByDataPublicacaoBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
    org.springframework.data.domain.Page<Article> findAll(org.springframework.data.domain.Pageable pageable);
}

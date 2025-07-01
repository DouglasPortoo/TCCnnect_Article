package com.api.tccArticle.services;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Article save(ArticleDTO article , String id);
    List<Article> findAll();
    Optional<Article> findById(String id);
    Article update( ArticleDTO articleDTO,String id, String articleId);
    void delete(String id);
    List<Article> findByCdAuthor(String cdAuthor);
}

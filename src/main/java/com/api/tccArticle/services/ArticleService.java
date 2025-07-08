package com.api.tccArticle.services;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Article save(String title, String resumo, List<String> palavrasChave, List<String> autores, String content,MultipartFile pdf, String id);
    List<Article> findAll();
    Optional<Article> findById(String id);
    Article update( ArticleDTO articleDTO,String id, String articleId);
    void delete(String id);
    List<Article> findByCdAuthor(String cdAuthor);
}

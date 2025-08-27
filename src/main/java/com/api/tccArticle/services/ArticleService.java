package com.api.tccArticle.services;

import com.api.tccArticle.domain.dto.ArticleRequestDTO;
import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import com.api.tccArticle.domain.dto.ArticleUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    ArticleResponseDTO save(ArticleRequestDTO article);
    List<ArticleResponseDTO> findAll();
    Optional<ArticleResponseDTO> findById(String id);
    ArticleResponseDTO update(ArticleUpdateDTO articleDTO);
    void delete(String id);
    List<ArticleResponseDTO> findByCdAuthor(String cdAuthor);
}

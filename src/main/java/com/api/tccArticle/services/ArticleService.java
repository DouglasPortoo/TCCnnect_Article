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
    List<ArticleResponseDTO> searchByTitle(String title);
    List<ArticleResponseDTO> getPaged(int page, int size, String sort);
    byte[] downloadPdf(String id);
    boolean updatePdf(String id, MultipartFile file);
    List<ArticleResponseDTO> getByStatus(String status);
    List<ArticleResponseDTO> getByCreatedBetween(String startDate, String endDate);
}

package com.api.tccArticle.domain.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ArticleDTO(@NotBlank String title, @NotBlank String content, String resumo, List<String> palavrasChave,
                         List<String> autores, MultipartFile pdf) {
}

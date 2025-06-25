package com.api.tccArticle.domain.dto;
import jakarta.validation.constraints.NotBlank;

public record ArticleDTO(@NotBlank String title, @NotBlank String content, @NotBlank String cdAuthor) {
}

package com.api.tccArticle.domain.dto;

import java.util.List;

public record ArticleResponseDTO( String cdArtigo,String title, String content, String resumo, List<String> palavrasChave,
                                 List<String> autores, String pdf) {
}

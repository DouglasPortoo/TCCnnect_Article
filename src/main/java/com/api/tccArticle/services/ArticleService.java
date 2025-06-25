package com.api.tccArticle.services;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;

public interface ArticleService {
    Article save(ArticleDTO article);
}

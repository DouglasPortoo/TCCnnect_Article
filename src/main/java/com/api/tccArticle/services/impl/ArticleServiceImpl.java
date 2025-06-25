package com.api.tccArticle.services.impl;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import com.api.tccArticle.repository.ArticleRepository;
import com.api.tccArticle.services.ArticleService;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repository;

    public  ArticleServiceImpl(ArticleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Article save(ArticleDTO article){


        Article newArticle = new Article();
        newArticle.setTitle(article.title());
        newArticle.setContent(article.content());
        newArticle.setCdAuthor(article.cdAuthor());

        return repository.save(newArticle);
    }

}

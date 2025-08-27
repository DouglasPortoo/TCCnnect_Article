package com.api.tccArticle.repository;

import com.api.tccArticle.domain.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article,String> {
    List<Article> findByCdAuthor(String cdAuthor);
}

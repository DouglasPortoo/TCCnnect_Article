package com.api.tccArticle.controller.impl;

import com.api.tccArticle.config.ArticleProducer;
import com.api.tccArticle.controller.ArticleController;
import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.tccArticle.services.ArticleService;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/articles")
public class ArticleControllerImpl implements ArticleController {

    private final ArticleService service;
    private final ArticleProducer producer;

    public ArticleControllerImpl(ArticleService service, ArticleProducer producer) {
        this.service = service;
        this.producer = producer;
    }

    @Override
    public ResponseEntity<Article>  create(@ModelAttribute @Valid ArticleDTO article , @PathVariable String id) {
        try {
            Article saved = service.save(
                    article.title(),
                    article.resumo(),
                    article.palavrasChave(),
                    article.autores(),
                    article.content(),
                    article.pdf(),
                    id);
            producer.publishNewArticle(saved);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<List<Article>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<Article> getById(String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Article> update( @RequestBody @Valid ArticleDTO article, @PathVariable String id, @PathVariable String articleId) {
        Article updated = service.update(article, id, articleId);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Article>> getByCdAuthor(String cdAuthor) {
        return ResponseEntity.ok(service.findByCdAuthor(cdAuthor));
    }
}

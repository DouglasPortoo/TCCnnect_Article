package com.api.tccArticle.controller;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ArticleController {

    @PostMapping( value ="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Article> create(@ModelAttribute @Valid ArticleDTO article, @PathVariable String id);

    @GetMapping
    ResponseEntity<List<Article>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<Article> getById(@PathVariable String id);

    @PutMapping("/{id}/{articleId}")
    ResponseEntity<Article> update( @RequestBody @Valid ArticleDTO article,@PathVariable String id, @PathVariable String articleId);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id);

    @GetMapping("/author/{cdAuthor}")
    ResponseEntity<List<Article>> getByCdAuthor(@PathVariable String cdAuthor);
}

package com.api.tccArticle.controller;

import com.api.tccArticle.domain.dto.ArticleRequestDTO;
import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import com.api.tccArticle.domain.dto.ArticleUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ArticleController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> create(@ModelAttribute @Valid ArticleRequestDTO article);

    @GetMapping
    ResponseEntity<List<ArticleResponseDTO>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<ArticleResponseDTO> getById(@PathVariable String id);

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> update(@ModelAttribute @Valid ArticleUpdateDTO article);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id);

    @GetMapping("/author/{cdAuthor}")
    ResponseEntity<List<ArticleResponseDTO>> getByCdAuthor(@PathVariable String cdAuthor);
}

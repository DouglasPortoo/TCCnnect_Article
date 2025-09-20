package com.api.tccArticle.controller.impl;

import com.api.tccArticle.config.ArticleProducer;
import com.api.tccArticle.controller.ArticleController;
import com.api.tccArticle.domain.dto.ArticleRequestDTO;
import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import com.api.tccArticle.domain.dto.ArticleUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.tccArticle.services.ArticleService;

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
    public ResponseEntity<Void> create(ArticleRequestDTO article) {
        try {
            ArticleResponseDTO saved = service.save(article);
            producer.publishNewArticle(saved);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<ArticleResponseDTO> getById(String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> update(ArticleUpdateDTO article) {
        try {
            ArticleResponseDTO saved = service.update(article);;
            producer.publishNewArticle(saved);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getByCdAuthor(String cdAuthor) {
        return ResponseEntity.ok(service.findByCdAuthor(cdAuthor));
    }

    @Override
    public ResponseEntity<List<ArticleResponseDTO>> searchByTitle(String title) {
        return ResponseEntity.ok(service.searchByTitle(title));
    }

    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getPaged(int page, int size, String sort) {
        return ResponseEntity.ok(service.getPaged(page, size, sort));
    }

    @Override
    public ResponseEntity<byte[]> downloadPdf(String id) {
        byte[] pdf = service.downloadPdf(id);
        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=article-" + id + ".pdf")
                .body(pdf);
    }

    @Override
    public ResponseEntity<Void> updatePdf(String id, org.springframework.web.multipart.MultipartFile file) {
        boolean updated = service.updatePdf(id, file);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getByStatus(String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getByCreatedBetween(String startDate, String endDate) {
        return ResponseEntity.ok(service.getByCreatedBetween(startDate, endDate));
    }
}

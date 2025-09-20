package com.api.tccArticle.controller.impl;

import com.api.tccArticle.config.ArticleProducer;
import com.api.tccArticle.domain.dto.ArticleRequestDTO;
import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import com.api.tccArticle.domain.dto.ArticleUpdateDTO;
import com.api.tccArticle.services.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ArticleControllerImplTest {

    @Mock
    private ArticleService service;

    @Mock
    private ArticleProducer producer;

    @InjectMocks
    private ArticleControllerImpl controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCreatedArticleWhenValidInputIsProvided() {
        ArticleRequestDTO dto = new ArticleRequestDTO(
                "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "publicado"
        );
        ArticleResponseDTO responseDTO = new ArticleResponseDTO(
                "1", "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null
        );

        when(service.save(any(ArticleRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<Void> response = controller.create(dto);

        verify(service).save(any(ArticleRequestDTO.class));
        verify(producer).publishNewArticle(responseDTO);
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() {
        ArticleRequestDTO dto = new ArticleRequestDTO(
                "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "publicado"
        );

        when(service.save(any(ArticleRequestDTO.class))).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Void> response = controller.create(dto);
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void shouldReturnAllArticles() {
        ArticleResponseDTO dto1 = new ArticleResponseDTO("1", "Título 1", "Conteúdo 1", "Resumo 1", List.of(), List.of(), null);
        ArticleResponseDTO dto2 = new ArticleResponseDTO("2", "Título 2", "Conteúdo 2", "Resumo 2", List.of(), List.of(), null);
        List<ArticleResponseDTO> articles = List.of(dto1, dto2);

        when(service.findAll()).thenReturn(articles);

        ResponseEntity<List<ArticleResponseDTO>> response = controller.getAll();

        assertEquals(articles, response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnArticleWhenIdExists() {
        ArticleResponseDTO dto = new ArticleResponseDTO("1", "Título", "Conteúdo", "Resumo", List.of(), List.of(), null);
        when(service.findById("1")).thenReturn(Optional.of(dto));

        ResponseEntity<ArticleResponseDTO> response = controller.getById("1");

        assertEquals(dto, response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() {
        when(service.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<ArticleResponseDTO> response = controller.getById("1");

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldUpdateArticleWhenValidInputIsProvided() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2", "publicado"
        );
        ArticleResponseDTO responseDTO = new ArticleResponseDTO(
                "2", "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null
        );

        when(service.update(any(ArticleUpdateDTO.class))).thenReturn(responseDTO);

        ResponseEntity<Void> response = controller.update(dto);

        verify(service).update(any(ArticleUpdateDTO.class));
        verify(producer).publishNewArticle(responseDTO);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUpdateThrowsException() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2", "publicado"
        );

        when(service.update(any(ArticleUpdateDTO.class))).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Void> response = controller.update(dto);
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void shouldDeleteArticle() {
        ResponseEntity<Void> response = controller.delete("1");
        verify(service).delete("1");
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnArticlesByCdAuthor() {
        ArticleResponseDTO dto = new ArticleResponseDTO("1", "Título", "Conteúdo", "Resumo", List.of(), List.of(), null);
        List<ArticleResponseDTO> articles = List.of(dto);

        when(service.findByCdAuthor("author1")).thenReturn(articles);

        ResponseEntity<List<ArticleResponseDTO>> response = controller.getByCdAuthor("author1");

        assertEquals(articles, response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnArticlesByTitle() {
        String title = "Spring";
        List<ArticleResponseDTO> articles = List.of(new ArticleResponseDTO("1", title, "content", "resumo", List.of(), List.of(), null));
        when(service.searchByTitle(title)).thenReturn(articles);
        ResponseEntity<List<ArticleResponseDTO>> response = controller.searchByTitle(title);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(articles, response.getBody());
    }

    @Test
    void shouldReturnPagedArticles() {
        List<ArticleResponseDTO> articles = List.of(new ArticleResponseDTO("1", "t", "c", "r", List.of(), List.of(), null));
        when(service.getPaged(0, 2, "createdAt,desc")).thenReturn(articles);
        ResponseEntity<List<ArticleResponseDTO>> response = controller.getPaged(0, 2, "createdAt,desc");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(articles, response.getBody());
    }

    @Test
    void shouldDownloadPdfSuccessfully() {
        String id = "1";
        byte[] pdf = new byte[]{1, 2, 3};
        when(service.downloadPdf(id)).thenReturn(pdf);
        ResponseEntity<byte[]> response = controller.downloadPdf(id);
        assertEquals(200, response.getStatusCode().value());
        assertArrayEquals(pdf, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPdfDoesNotExist() {
        String id = "2";
        when(service.downloadPdf(id)).thenReturn(null);
        ResponseEntity<byte[]> response = controller.downloadPdf(id);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldUpdatePdfSuccessfully() {
        String id = "1";
        org.springframework.web.multipart.MultipartFile file = null;
        when(service.updatePdf(id, file)).thenReturn(true);
        ResponseEntity<Void> response = controller.updatePdf(id, file);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatePdfFails() {
        String id = "2";
        org.springframework.web.multipart.MultipartFile file = null;
        when(service.updatePdf(id, file)).thenReturn(false);
        ResponseEntity<Void> response = controller.updatePdf(id, file);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldReturnArticlesByStatus() {
        String status = "publicado";
        List<ArticleResponseDTO> articles = List.of(new ArticleResponseDTO("1", "t", "c", "r", List.of(), List.of(), null));
        when(service.getByStatus(status)).thenReturn(articles);
        ResponseEntity<List<ArticleResponseDTO>> response = controller.getByStatus(status);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(articles, response.getBody());
    }

    @Test
    void shouldReturnArticlesByCreatedBetween() {
        String start = "2024-01-01T00:00:00";
        String end = "2024-12-31T23:59:59";
        List<ArticleResponseDTO> articles = List.of(new ArticleResponseDTO("1", "t", "c", "r", List.of(), List.of(), null));
        when(service.getByCreatedBetween(start, end)).thenReturn(articles);
        ResponseEntity<List<ArticleResponseDTO>> response = controller.getByCreatedBetween(start, end);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(articles, response.getBody());
    }
}
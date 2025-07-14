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
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1"
        );
        ArticleResponseDTO responseDTO = new ArticleResponseDTO(
                "1", "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null
        );

        when(service.save(any(ArticleRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<Void> response = controller.create(dto);

        verify(service).save(any(ArticleRequestDTO.class));
        verify(producer).publishNewArticle(responseDTO);
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() {
        ArticleRequestDTO dto = new ArticleRequestDTO(
                "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1"
        );

        when(service.save(any(ArticleRequestDTO.class))).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Void> response = controller.create(dto);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnAllArticles() {
        ArticleResponseDTO dto1 = new ArticleResponseDTO("1", "Título 1", "Conteúdo 1", "Resumo 1", List.of(), List.of(), null);
        ArticleResponseDTO dto2 = new ArticleResponseDTO("2", "Título 2", "Conteúdo 2", "Resumo 2", List.of(), List.of(), null);
        List<ArticleResponseDTO> articles = List.of(dto1, dto2);

        when(service.findAll()).thenReturn(articles);

        ResponseEntity<List<ArticleResponseDTO>> response = controller.getAll();

        assertEquals(articles, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnArticleWhenIdExists() {
        ArticleResponseDTO dto = new ArticleResponseDTO("1", "Título", "Conteúdo", "Resumo", List.of(), List.of(), null);
        when(service.findById("1")).thenReturn(Optional.of(dto));

        ResponseEntity<ArticleResponseDTO> response = controller.getById("1");

        assertEquals(dto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() {
        when(service.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<ArticleResponseDTO> response = controller.getById("1");

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldUpdateArticleWhenValidInputIsProvided() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2"
        );
        ArticleResponseDTO responseDTO = new ArticleResponseDTO(
                "2", "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null
        );

        when(service.update(any(ArticleUpdateDTO.class))).thenReturn(responseDTO);

        ResponseEntity<Void> response = controller.update(dto);

        verify(service).update(any(ArticleUpdateDTO.class));
        verify(producer).publishNewArticle(responseDTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUpdateThrowsException() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2"
        );

        when(service.update(any(ArticleUpdateDTO.class))).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Void> response = controller.update(dto);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteArticle() {
        ResponseEntity<Void> response = controller.delete("1");
        verify(service).delete("1");
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnArticlesByCdAuthor() {
        ArticleResponseDTO dto = new ArticleResponseDTO("1", "Título", "Conteúdo", "Resumo", List.of(), List.of(), null);
        List<ArticleResponseDTO> articles = List.of(dto);

        when(service.findByCdAuthor("author1")).thenReturn(articles);

        ResponseEntity<List<ArticleResponseDTO>> response = controller.getByCdAuthor("author1");

        assertEquals(articles, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
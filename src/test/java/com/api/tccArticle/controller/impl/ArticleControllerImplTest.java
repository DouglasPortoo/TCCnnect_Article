package com.api.tccArticle.controller.impl;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import com.api.tccArticle.services.ArticleService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ArticleControllerImplTest {

    @Mock
    private ArticleService service;

    @InjectMocks
    private ArticleControllerImpl controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCreatedArticleWhenValidInputIsProvided() {
        ArticleDTO dto = new ArticleDTO("Meu Título", "Meu Conteúdo");
        Article article = new Article();
        article.setTitle("Meu Título");
        article.setContent("Meu Conteúdo");

        // Captura o argumento passado ao service.save
        ArgumentCaptor<ArticleDTO> captor = ArgumentCaptor.forClass(ArticleDTO.class);
        when(service.save(captor.capture(), anyString())).thenReturn(article);

        ResponseEntity<Article> response = controller.create(dto, "1");

        // Verifica se o DTO passado ao service tem os valores esperados
        ArticleDTO dtoCapturado = captor.getValue();

        assertEquals("Meu Título", dtoCapturado.title());
        assertEquals("Meu Conteúdo", dtoCapturado.content());
        assertEquals(article, response.getBody());
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInputIsProvided() {
        ArticleDTO invalidDto = new ArticleDTO(null, null);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(invalidDto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() {
        ArticleDTO dto = new ArticleDTO("Título", "Conteúdo");
        when(service.save(any(ArticleDTO.class), anyString())).thenThrow(new RuntimeException("Erro interno"));

        Exception exception = assertThrows(RuntimeException.class, () -> controller.create(dto, "1"));
        assertEquals("Erro interno", exception.getMessage());
    }


    @Test
    void shouldReturnAllArticles() {
        Article article1 = new Article();
        Article article2 = new Article();
        List<Article> articles = List.of(article1, article2);

        when(service.findAll()).thenReturn(articles);

        ResponseEntity<List<Article>> response = controller.getAll();

        assertEquals(articles, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnArticleWhenIdExists() {
        Article article = new Article();
        when(service.findById("1")).thenReturn(Optional.of(article));

        ResponseEntity<Article> response = controller.getById("1");

        assertEquals(article, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() {
        when(service.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Article> response = controller.getById("1");

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldUpdateArticleWhenValidInputIsProvided() {
        ArticleDTO dto = new ArticleDTO("Novo Título", "Novo Conteúdo");
        Article updated = new Article();
        updated.setTitle("Novo Título");
        updated.setContent("Novo Conteúdo");

        ArgumentCaptor<ArticleDTO> captor = ArgumentCaptor.forClass(ArticleDTO.class);
        when(service.update(captor.capture(), anyString(), anyString())).thenReturn(updated);

        ResponseEntity<Article> response = controller.update(dto, "1", "2");

        ArticleDTO dtoCapturado = captor.getValue();
        assertEquals("Novo Título", dtoCapturado.title());
        assertEquals("Novo Conteúdo", dtoCapturado.content());
        assertEquals(updated, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteArticle() {
        ResponseEntity<Void> response = controller.delete("1");
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnArticlesByCdAuthor() {
        Article article = new Article();
        List<Article> articles = List.of(article);

        when(service.findByCdAuthor("author1")).thenReturn(articles);

        ResponseEntity<List<Article>> response = controller.getByCdAuthor("author1");

        assertEquals(articles, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
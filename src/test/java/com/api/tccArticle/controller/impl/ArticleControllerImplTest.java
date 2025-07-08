package com.api.tccArticle.controller.impl;

import com.api.tccArticle.config.ArticleProducer;
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
import org.springframework.web.multipart.MultipartFile;

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
        ArticleDTO dto = new ArticleDTO("Meu Título", "Meu Conteúdo", "Resumo do artigo", List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null);
        Article article = new Article();
        article.setTitle("Meu Título");
        article.setContent("Meu Conteúdo");

        // Captura o argumento passado ao service.save
        ArgumentCaptor<ArticleDTO> captor = ArgumentCaptor.forClass(ArticleDTO.class);
        when(service.save(anyString(), anyString(), anyList(), anyList(), anyString(), any(MultipartFile.class), anyString())).thenReturn(article);

        ResponseEntity<Article> response = controller.create(dto, "1");

        // Verifica se o DTO passado ao service tem os valores esperados
        verify(service).save(
                eq("Meu Título"),
                eq("Resumo do artigo"),
                eq(List.of("palavra1", "palavra2")),
                eq(List.of("Autor 1", "Autor 2")),
                eq("Meu Conteúdo"),
                isNull(),
                eq("1")
        );

        // Verifica se o producer foi chamado
//        verify(producer).publishNewArticle(article);

        // Verifica o corpo e o status da resposta
//        assertEquals(article, response.getBody());
//        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInputIsProvided() {
        ArticleDTO invalidDto = new ArticleDTO("", "Meu Conteúdo", "Resumo do artigo", List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(invalidDto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() {
        ArticleDTO dto = new ArticleDTO("", "Meu Conteúdo", "Resumo do artigo", List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null);

        when(service.save(
                anyString(),
                anyString(),
                anyList(),
                anyList(),
                anyString(),
                nullable(MultipartFile.class),
                anyString()
        )).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Article> response = controller.create(dto, "1");
        assertEquals(500, response.getStatusCodeValue());
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
        ArticleDTO dto = new ArticleDTO("Novo Título", "Novo Conteúdo", "Resumo do artigo", List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null);
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
package com.api.tccArticle.services.impl;

import com.api.tccArticle.domain.dto.ArticleRequestDTO;
import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import com.api.tccArticle.domain.dto.ArticleUpdateDTO;
import com.api.tccArticle.domain.dto.UsuarioDTO;
import com.api.tccArticle.domain.model.Article;
import com.api.tccArticle.openfeign.request.UsuarioClient;
import com.api.tccArticle.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ArticleServiceImplTest {

    private ArticleRepository repository;
    private UsuarioClient usuarioClient;
    private ArticleServiceImpl service;
    private PdfStorageServiceImpl pdfStorageService;

    @BeforeEach
    void setUp() {
        repository = mock(ArticleRepository.class);
        usuarioClient = mock(UsuarioClient.class);
        pdfStorageService = mock(PdfStorageServiceImpl.class);
        service = new ArticleServiceImpl(repository, usuarioClient, pdfStorageService);
    }

    @Test
    void shouldSaveArticleWhenUserIdIsValid() {
        ArticleRequestDTO dto = new ArticleRequestDTO(
                "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1"
        );
        Article saved = new Article();
        saved.setTitle("Meu Título");
        saved.setContent("Meu Conteúdo");
        saved.setCdAuthor("123");

        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("123");
        when(repository.save(any(Article.class))).thenReturn(saved);

        ArticleResponseDTO result = service.save(dto);

        assertEquals("Meu Título", result.title());
        assertEquals("Meu Conteúdo", result.content());
    }

    @Test
    void shouldReturnAllArticles() {
        Article article = new Article();
        article.setCdArtigo("1");
        article.setTitle("Título");
        article.setContent("Conteúdo");
        when(repository.findAll()).thenReturn(List.of(article));

        List<ArticleResponseDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Título", result.get(0).title());
    }

    @Test
    void shouldReturnArticleById() {
        Article article = new Article();
        article.setCdArtigo("1");
        article.setTitle("Título");
        when(repository.findById("1")).thenReturn(Optional.of(article));

        Optional<ArticleResponseDTO> result = service.findById("1");

        assertTrue(result.isPresent());
        assertEquals("Título", result.get().title());
    }

    @Test
    void shouldReturnArticlesByCdAuthor() {
        Article article = new Article();
        article.setCdArtigo("1");
        article.setTitle("Título");
        when(repository.findByCdAuthor("author")).thenReturn(List.of(article));

        List<ArticleResponseDTO> result = service.findByCdAuthor("author");

        assertEquals(1, result.size());
        assertEquals("Título", result.get(0).title());
    }

    @Test
    void shouldUpdateArticleWhenValid() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2"
        );
        Article existing = new Article();
        existing.setCdArtigo("2");
        existing.setTitle("Antigo");
        Article updated = new Article();
        updated.setCdArtigo("2");
        updated.setTitle("Novo Título");
        updated.setContent("Novo Conteúdo");

        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);
        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("123");
        when(repository.findById("2")).thenReturn(Optional.of(existing));
        when(repository.save(any(Article.class))).thenReturn(updated);

        ArticleResponseDTO result = service.update(dto);

        assertEquals("Novo Título", result.title());
        assertEquals("Novo Conteúdo", result.content());
    }

    @Test
    void shouldThrowExceptionWhenArticleNotFoundOnUpdate() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2"
        );
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);
        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("123");
        when(repository.findById("2")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.update(dto));
    }

    @Test
    void shouldDeleteArticle() {
        Article article = new Article();
        article.setCdArtigo("1");
        when(repository.findById("1")).thenReturn(Optional.of(article));

        service.delete("1");
        verify(repository).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNullOnSave() {
        ArticleRequestDTO dto = new ArticleRequestDTO(
                "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1"
        );
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsEmptyOnSave() {
        ArticleRequestDTO dto = new ArticleRequestDTO(
                "Meu Título", "Meu Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1"
        );
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNullOnUpdate() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2"
        );
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.update(dto));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsEmptyOnUpdate() {
        ArticleUpdateDTO dto = new ArticleUpdateDTO(
                "Novo Título", "Novo Conteúdo", "Resumo do artigo",
                List.of("palavra1", "palavra2"), List.of("Autor 1", "Autor 2"), null, "1", "2"
        );
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> service.update(dto));
    }
}
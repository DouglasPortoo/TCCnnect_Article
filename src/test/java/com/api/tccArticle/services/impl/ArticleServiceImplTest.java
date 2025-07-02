package com.api.tccArticle.services.impl;

import com.api.tccArticle.domain.dto.ArticleDTO;
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


    @BeforeEach
    void setUp() {
        repository = mock(ArticleRepository.class);
        usuarioClient = mock(UsuarioClient.class);
        service = new ArticleServiceImpl(repository, usuarioClient);
    }

    @Test
    void shouldSaveArticleWhenUserIdIsValid() {
        ArticleDTO dto = new ArticleDTO("Título", "Conteúdo");
        Article saved = new Article();
        saved.setTitle("Título");
        saved.setContent("Conteúdo");
        saved.setCdAuthor("123");

        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("123");
        when(repository.save(any(Article.class))).thenReturn(saved);

        Article result = service.save(dto, "1");

        assertEquals("Título", result.getTitle());
        assertEquals("Conteúdo", result.getContent());
        assertEquals("123", result.getCdAuthor());
    }

    @Test
    void shouldReturnAllArticles() {
        Article article = new Article();
        when(repository.findAll()).thenReturn(List.of(article));

        List<Article> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(article, result.get(0));
    }

    @Test
    void shouldReturnArticleById() {
        Article article = new Article();
        when(repository.findById("1")).thenReturn(Optional.of(article));

        Optional<Article> result = service.findById("1");

        assertTrue(result.isPresent());
        assertEquals(article, result.get());
    }

    @Test
    void shouldReturnArticlesByCdAuthor() {
        Article article = new Article();
        when(repository.findByCdAuthor("author")).thenReturn(List.of(article));

        List<Article> result = service.findByCdAuthor("author");

        assertEquals(1, result.size());
        assertEquals(article, result.get(0));
    }

    @Test
    void shouldUpdateArticleWhenValid() {
        ArticleDTO dto = new ArticleDTO("Novo", "Conteúdo");
        Article existing = new Article();
        existing.setCdAuthor("old");
        Article updated = new Article();
        updated.setTitle("Novo");
        updated.setContent("Conteúdo");
        updated.setCdAuthor("123");

        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);
        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("123");
        when(repository.findById("2")).thenReturn(Optional.of(existing));
        when(repository.save(any(Article.class))).thenReturn(updated);

        Article result = service.update(dto, "1", "2");

        assertEquals("Novo", result.getTitle());
        assertEquals("Conteúdo", result.getContent());
        assertEquals("123", result.getCdAuthor());
    }

    @Test
    void shouldThrowExceptionWhenArticleNotFoundOnUpdate() {
        ArticleDTO dto = new ArticleDTO("Novo", "Conteúdo");
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);
        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("123");
        when(repository.findById("2")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.update(dto, "1", "2"));
    }

    @Test
    void shouldDeleteArticle() {
        service.delete("1");
        verify(repository).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNullOnSave() {
        ArticleDTO dto = new ArticleDTO("Título", "Conteúdo");
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.save(dto, "1"));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsEmptyOnSave() {
        ArticleDTO dto = new ArticleDTO("Título", "Conteúdo");
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> service.save(dto, "1"));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNullOnUpdate() {
        ArticleDTO dto = new ArticleDTO("Novo", "Conteúdo");
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.update(dto, "1", "2"));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsEmptyOnUpdate() {
        ArticleDTO dto = new ArticleDTO("Novo", "Conteúdo");
        var usuarioResponse = mock(ResponseEntity.class);
        var usuarioBody = mock(UsuarioDTO.class);

        when(usuarioClient.buscarPorId("1")).thenReturn(usuarioResponse);
        when(usuarioResponse.getBody()).thenReturn(usuarioBody);
        when(usuarioBody.cdUsuario()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> service.update(dto, "1", "2"));
    }

}
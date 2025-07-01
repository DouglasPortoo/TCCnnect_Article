package com.api.tccArticle.services.impl;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import com.api.tccArticle.openfeign.request.UsuarioClient;
import com.api.tccArticle.repository.ArticleRepository;
import com.api.tccArticle.services.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repository;
    private final UsuarioClient usuarioClient;

    public  ArticleServiceImpl(ArticleRepository repository, UsuarioClient usuarioClient) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
    }

    @Override
    public Article save(ArticleDTO article, String id){

        var usuarioResponse = usuarioClient.buscarPorId(id);
        String IdUsuario = usuarioResponse.getBody().cdUsuario();

        if (IdUsuario == null || IdUsuario.isEmpty()) {
            throw new IllegalArgumentException("Id não encontrado ou nome inválido.");
        }

        Article newArticle = new Article();
        newArticle.setTitle(article.title());
        newArticle.setContent(article.content());
        newArticle.setCdAuthor(IdUsuario);

        return repository.save(newArticle);
    }

    @Override
    public List<Article> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Article> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Article> findByCdAuthor(String cdAuthor) {
        return repository.findByCdAuthor(cdAuthor);
    }

    @Override
    public Article update( ArticleDTO articleDTO,String id, String articleId) {
        var usuarioResponse = usuarioClient.buscarPorId(id);
        String IdUsuario = usuarioResponse.getBody().cdUsuario();

        if (IdUsuario == null || IdUsuario.isEmpty()) {
            throw new IllegalArgumentException("Id não encontrado ou nome inválido.");
        }

        Article article = repository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado"));
        article.setTitle(articleDTO.title());
        article.setContent(articleDTO.content());
        article.setCdAuthor(IdUsuario);

        return repository.save(article);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}

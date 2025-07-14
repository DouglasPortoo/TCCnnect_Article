package com.api.tccArticle.services.impl;

import com.api.tccArticle.domain.dto.ArticleRequestDTO;
import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import com.api.tccArticle.domain.dto.ArticleUpdateDTO;
import com.api.tccArticle.domain.model.Article;
import com.api.tccArticle.openfeign.request.UsuarioClient;
import com.api.tccArticle.repository.ArticleRepository;
import com.api.tccArticle.services.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repository;
    private final UsuarioClient usuarioClient;
    private final PdfStorageServiceImpl pdfStorageService;

    public ArticleServiceImpl(ArticleRepository repository, UsuarioClient usuarioClient, PdfStorageServiceImpl pdfStorageService) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
        this.pdfStorageService = pdfStorageService;
    }

    @Override
    public ArticleResponseDTO save(ArticleRequestDTO articleRequestDTO) {

        var usuarioResponse = usuarioClient.buscarPorId(articleRequestDTO.id());
        String usuarioId = usuarioResponse.getBody().cdUsuario();

        if (usuarioId == null || usuarioId.isEmpty()) {
            throw new IllegalArgumentException("Id não encontrado ou nome inválido.");
        }

        String pdfUrl = null;
        if (articleRequestDTO.pdf() != null && !articleRequestDTO.pdf().isEmpty()) {
            pdfUrl = pdfStorageService.save(articleRequestDTO.pdf());
        }

        Article article = new Article();
        article.setTitle(articleRequestDTO.title());
        article.setResumo(articleRequestDTO.resumo());
        article.setPalavrasChave(articleRequestDTO.palavrasChave());
        article.setAutores(articleRequestDTO.autores());
        article.setPdfUrl(pdfUrl);
        article.setCdAuthor(articleRequestDTO.id());
        article.setContent(articleRequestDTO.content());
        article.setDataPublicacao(LocalDateTime.now());

        repository.save(article);

        return new ArticleResponseDTO(
                article.getCdArtigo(),
                article.getTitle(),
                article.getContent(),
                article.getResumo(),
                article.getPalavrasChave(),
                article.getAutores(),
                article.getPdfUrl()
        );
    }

    @Override
    public List<ArticleResponseDTO> findAll() {

        List<Article> articles = repository.findAll();
        return articles.stream()
                .map(article -> new ArticleResponseDTO(
                        article.getCdArtigo(),
                        article.getTitle(),
                        article.getContent(),
                        article.getResumo(),
                        article.getPalavrasChave(),
                        article.getAutores(),
                        article.getPdfUrl()))
                .toList();
    }

    @Override
    public Optional<ArticleResponseDTO> findById(String id) {

        Optional<Article> articleEntity = repository.findById(id);

        return articleEntity.map(a -> new ArticleResponseDTO(
                a.getCdArtigo(),
                a.getTitle(),
                a.getContent(),
                a.getResumo(),
                a.getPalavrasChave(),
                a.getAutores(),
                a.getPdfUrl()));
    }

    @Override
    public List<ArticleResponseDTO> findByCdAuthor(String cdAuthor) {

        List<Article> all = repository.findByCdAuthor(cdAuthor);
        return all.stream()
                .map(article -> new ArticleResponseDTO(
                        article.getCdArtigo(),
                        article.getTitle(),
                        article.getContent(),
                        article.getResumo(),
                        article.getPalavrasChave(),
                        article.getAutores(),
                        article.getPdfUrl()))
                .toList();
    }

    @Override
    public ArticleResponseDTO update(ArticleUpdateDTO articleDTO) {

        var usuarioResponse = usuarioClient.buscarPorId(articleDTO.id());
        String IdUsuario = usuarioResponse.getBody().cdUsuario();

        if (IdUsuario == null || IdUsuario.isEmpty()) {
            throw new IllegalArgumentException("Id não encontrado ou nome inválido.");
        }

        Article article = repository.findById(articleDTO.articleId())
                .orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado"));

        if (articleDTO.pdf() != null && !articleDTO.pdf().isEmpty()) {
            // Deleta o PDF antigo, se existir
            String oldPdfUrl = article.getPdfUrl();
            if (oldPdfUrl != null) {
                try {
                    Files.deleteIfExists(Paths.get(oldPdfUrl));
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao deletar o PDF antigo", e);
                }
            }
            // Salva o novo PDF
            String pdfUrl = pdfStorageService.save(articleDTO.pdf());
            article.setPdfUrl(pdfUrl);
        }

        if (articleDTO.title() != null && !articleDTO.title().isEmpty()) article.setTitle(articleDTO.title());
        if (articleDTO.content() != null && !articleDTO.content().isEmpty()) article.setContent(articleDTO.content());
        if (articleDTO.resumo() != null && !articleDTO.resumo().isEmpty()) article.setResumo(articleDTO.resumo());
        if (articleDTO.palavrasChave() != null && !articleDTO.palavrasChave().isEmpty())
            article.setPalavrasChave(articleDTO.palavrasChave());
        if (articleDTO.autores() != null && !articleDTO.autores().isEmpty()) article.setAutores(articleDTO.autores());

        repository.save(article);

        return new ArticleResponseDTO(
                article.getCdArtigo(),
                article.getTitle(),
                article.getContent(),
                article.getResumo(),
                article.getPalavrasChave(),
                article.getAutores(),
                article.getPdfUrl()
        );
    }

    @Override
    public void delete(String id) {
        Article article = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado"));

        String pdfUrl = article.getPdfUrl();
        if (pdfUrl != null) {
            try {
                Files.deleteIfExists(Paths.get(pdfUrl));
            } catch (IOException e) {
                throw new RuntimeException("Erro ao deletar o PDF do artigo", e);
            }
        }

        repository.deleteById(id);
    }
}

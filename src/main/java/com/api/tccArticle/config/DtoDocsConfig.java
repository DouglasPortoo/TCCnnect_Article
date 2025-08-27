package com.api.tccArticle.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DtoDocsConfig {
    @Bean
    public OpenApiCustomizer dtoCustomiser() {
        return openApi -> {
            Components components = openApi.getComponents();

            // UsuarioDTO
            components.addSchemas("UsuarioDTO", new Schema<>()
                    .type("object")
                    .addProperties("cdUsuario", new Schema<>()
                            .type("string")
                            .description("Identificador único do usuário"))
                    .addProperties("nmUsuario", new Schema<>()
                            .type("string")
                            .description("Nome do usuário")));

            // ArticleRequestDTO
            components.addSchemas("ArticleRequestDTO", new Schema<>()
                    .type("object")
                    .addProperties("title", new Schema<>().type("string").description("Título do artigo"))
                    .addProperties("content", new Schema<>().type("string").description("Conteúdo do artigo"))
                    .addProperties("resumo", new Schema<>().type("string").description("Resumo do artigo"))
                    .addProperties("palavrasChave", new Schema<>().type("array").items(new Schema<>().type("string")).description("Palavras-chave"))
                    .addProperties("autores", new Schema<>().type("array").items(new Schema<>().type("string")).description("Autores"))
                    .addProperties("pdf", new Schema<>().type("string").format("binary").description("Arquivo PDF"))
                    .addProperties("id", new Schema<>().type("string").description("ID do usuário")));

            // ArticleResponseDTO
            components.addSchemas("ArticleResponseDTO", new Schema<>()
                    .type("object")
                    .addProperties("cdArtigo", new Schema<>().type("string").description("ID do artigo"))
                    .addProperties("title", new Schema<>().type("string").description("Título do artigo"))
                    .addProperties("content", new Schema<>().type("string").description("Conteúdo do artigo"))
                    .addProperties("resumo", new Schema<>().type("string").description("Resumo do artigo"))
                    .addProperties("palavrasChave", new Schema<>().type("array").items(new Schema<>().type("string")).description("Palavras-chave"))
                    .addProperties("autores", new Schema<>().type("array").items(new Schema<>().type("string")).description("Autores"))
                    .addProperties("pdf", new Schema<>().type("string").description("URL do PDF")));

            // ArticleUpdateDTO
            components.addSchemas("ArticleUpdateDTO", new Schema<>()
                    .type("object")
                    .addProperties("title", new Schema<>().type("string").description("Título do artigo"))
                    .addProperties("content", new Schema<>().type("string").description("Conteúdo do artigo"))
                    .addProperties("resumo", new Schema<>().type("string").description("Resumo do artigo"))
                    .addProperties("palavrasChave", new Schema<>().type("array").items(new Schema<>().type("string")).description("Palavras-chave"))
                    .addProperties("autores", new Schema<>().type("array").items(new Schema<>().type("string")).description("Autores"))
                    .addProperties("pdf", new Schema<>().type("string").format("binary").description("Arquivo PDF"))
                    .addProperties("id", new Schema<>().type("string").description("ID do usuário"))
                    .addProperties("articleId", new Schema<>().type("string").description("ID do artigo")));
        };
    }
}

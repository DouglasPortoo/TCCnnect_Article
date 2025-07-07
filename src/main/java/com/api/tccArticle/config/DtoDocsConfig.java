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

            // Esquema do UsuarioDTO
            components.addSchemas("UsuarioDTO", new Schema<>()
                    .type("object")
                    .addProperties("id", new Schema<>()
                            .type("string")
                            .description("Identificador único do usuário"))
                    .addProperties("name", new Schema<>()
                            .type("string")
                            .description("Nome do usuário")));

            // Esquema do ArticleDTO
            components.addSchemas("ArticleDTO", new Schema<>()
                    .type("object")
                    .addProperties("title", new Schema<>()
                            .type("string")
                            .description("Título do artigo"))
                    .addProperties("content", new Schema<>()
                            .type("string")
                            .description("Conteúdo do artigo")));
        };
    }
}

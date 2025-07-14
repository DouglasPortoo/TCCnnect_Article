package com.api.tccArticle.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArticleControllerDocsConfig {
    @Bean
    public OpenApiCustomizer articleControllerCustomizer() {
        return openApi -> {
            Paths paths = openApi.getPaths();

            // /articles: POST, GET, PUT
            PathItem articlesPath = new PathItem()
                    .post(createArticleOperation())
                    .get(getAllArticlesOperation())
                    .put(updateArticleOperation());
            paths.addPathItem("/articles", articlesPath);

            // /articles/{id}: GET, DELETE
            PathItem articlesIdPath = new PathItem()
                    .get(getArticleByIdOperation())
                    .delete(deleteArticleOperation());
            paths.addPathItem("/articles/{id}", articlesIdPath);

            // /articles/author/{cdAuthor}: GET
            paths.addPathItem("/articles/author/{cdAuthor}", new PathItem().get(getByCdAuthorOperation()));
        };
    }

    private Operation createArticleOperation() {
        return new Operation()
                .summary("Cria um novo artigo")
                .description("Cria um artigo para o usuário informado.")
                .addTagsItem("Article")
                .requestBody(new RequestBody()
                        .description("Dados do artigo")
                        .required(true)
                        .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ArticleRequestDTO")))))
                .responses(new ApiResponses()
                        .addApiResponse("201", new ApiResponse().description("Artigo criado com sucesso"))
                        .addApiResponse("500", new ApiResponse().description("Erro interno")));
    }

    private Operation getAllArticlesOperation() {
        return new Operation()
                .summary("Lista todos os artigos")
                .description("Retorna todos os artigos cadastrados.")
                .addTagsItem("Article")
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("Lista de artigos")));
    }

    private Operation getArticleByIdOperation() {
        return new Operation()
                .summary("Busca artigo por ID")
                .description("Retorna um artigo pelo seu ID.")
                .addTagsItem("Article")
                .addParametersItem(new Parameter()
                        .name("id")
                        .in("path")
                        .required(true)
                        .schema(new Schema<>().type("string")))
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("Artigo encontrado"))
                        .addApiResponse("404", new ApiResponse().description("Artigo não encontrado")));
    }

    private Operation updateArticleOperation() {
        return new Operation()
                .summary("Atualiza um artigo")
                .description("Atualiza um artigo existente.")
                .addTagsItem("Article")
                .requestBody(new RequestBody()
                        .description("Dados do artigo para atualização")
                        .required(true)
                        .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ArticleUpdateDTO")))))
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("Artigo atualizado"))
                        .addApiResponse("500", new ApiResponse().description("Erro interno")));
    }

    private Operation deleteArticleOperation() {
        return new Operation()
                .summary("Remove um artigo")
                .description("Remove um artigo pelo ID.")
                .addTagsItem("Article")
                .addParametersItem(new Parameter()
                        .name("id")
                        .in("path")
                        .required(true)
                        .schema(new Schema<>().type("string")))
                .responses(new ApiResponses()
                        .addApiResponse("204", new ApiResponse().description("Artigo removido")));
    }

    private Operation getByCdAuthorOperation() {
        return new Operation()
                .summary("Busca artigos por autor")
                .description("Retorna todos os artigos de um autor.")
                .addTagsItem("Article")
                .addParametersItem(new Parameter()
                        .name("cdAuthor")
                        .in("path")
                        .required(true)
                        .schema(new Schema<>().type("string")))
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("Lista de artigos do autor")));
    }
}
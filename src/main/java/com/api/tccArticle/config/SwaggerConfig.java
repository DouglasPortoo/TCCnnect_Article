package com.api.tccArticle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TCCnnect-Article API")
                        .version("1.0.0")
                        .description("Documentação da API do TCCnnect Article").contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Equipe TCCnnect")
                                .email("contato@tccnnect.com"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}

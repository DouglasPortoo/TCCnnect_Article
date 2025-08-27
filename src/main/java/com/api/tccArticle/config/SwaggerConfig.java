package com.api.tccArticle.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:swagger.properties")
public class SwaggerConfig {

    @Value("${swagger.info.title}")
    private String title;
    @Value("${swagger.info.description}")
    private String description;
    @Value("${application.build.version}")
    private String version;
    @Value("${swagger.info.termsOfServiceUrl}")
    private String termsOfServiceUrl;
    @Value("${swagger.info.contactName}")
    private String contactName;
    @Value("${swagger.info.contactUrl}")
    private String contactUrl;
    @Value("${swagger.info.contactEmail}")
    private String contactEmail;
    @Value("${swagger.info.license}")
    private String license;
    @Value("${swagger.info.licenseUrl}")
    private String licenseUrl;
    @Value("${application.swagger.url}")
    private String urlSwagger;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addServersItem(new Server().url(urlSwagger.concat(contextPath)))
                .components(new Components().addSecuritySchemes(AUTHORIZATION_HEADER,
                        new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER).name(AUTHORIZATION_HEADER)))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION_HEADER));
    }

    private Info apiInfo() {
        return new Info()
                .title(title)
                .description(description)
                .version(version)
                .termsOfService(termsOfServiceUrl)
                .license(new License().name(license).url(licenseUrl))
                .contact(new Contact().name(contactName).url(contactUrl).email(contactEmail));
    }
}

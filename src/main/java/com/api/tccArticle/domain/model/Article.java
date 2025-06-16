package com.api.tccArticle.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document(collection = "tb_article")
public class Article {

    @MongoId
    private String cdArtigo;
    private String title;
    private String content;
    private String cdAuthor;
    private LocalDateTime dataPublicacao;
}

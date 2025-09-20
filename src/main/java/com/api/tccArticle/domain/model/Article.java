package com.api.tccArticle.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_article")
public class Article {

    @MongoId
    private String cdArtigo;
    private String title;
    private String resumo;
    private List<String> palavrasChave;
    private List<String> autores;
    private String content;
    private String cdAuthor;
    private String pdfUrl;
    @CreatedDate
    private LocalDateTime dataPublicacao;
    private String status;
}

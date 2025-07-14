package com.api.tccArticle.config;

import com.api.tccArticle.domain.dto.ArticleResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArticleProducer {
    private final KafkaTemplate<String, ArticleResponseDTO> kafkaTemplate;

    public ArticleProducer(KafkaTemplate<String, ArticleResponseDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishNewArticle(ArticleResponseDTO article) {
        kafkaTemplate.send("novo-artigo-publicado", article);
    }
}

package com.api.tccArticle.config;

import com.api.tccArticle.domain.dto.ArticleDTO;
import com.api.tccArticle.domain.model.Article;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArticleProducer {
    private final KafkaTemplate<String, Article> kafkaTemplate;

    public ArticleProducer(KafkaTemplate<String, Article> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishNewArticle(Article article) {
        kafkaTemplate.send("novo-artigo-publicado", article);
    }
}

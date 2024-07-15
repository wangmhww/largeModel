package com.wm.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiDemoApplication {

    @Bean
    public RedisVectorStore vectorStore(EmbeddingModel embeddingModel){
       RedisVectorStore.RedisVectorStoreConfig vectorStoreConfig = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withURI("redis://localhost:6379")
                .withMetadataFields(
                        RedisVectorStore.MetadataField.text("filename"),
                        RedisVectorStore.MetadataField.text("question")
                )
                .build();
        return new RedisVectorStore(vectorStoreConfig, embeddingModel, true);
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringAiDemoApplication.class, args);
    }

}

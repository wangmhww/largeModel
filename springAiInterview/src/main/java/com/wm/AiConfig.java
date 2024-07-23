package com.wm;

import org.springframework.ai.autoconfigure.ollama.OllamaEmbeddingProperties;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AiConfig {

    @Bean
    public RedisVectorStore vectorStore(EmbeddingClient embeddingClient){
        RedisVectorStore.RedisVectorStoreConfig vectorStoreConfig = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withURI("redis://localhost:6379")
                .withIndexName("interview-assistant-index")
                .withMetadataFields(
                        RedisVectorStore.MetadataField.text("filename")
                ).build();
        return  new RedisVectorStore( vectorStoreConfig, embeddingClient);
    }

    @Bean
    public CustomerOllamaEmbeddingClient ollamaEmbeddingClient(OllamaApi ollamaApi){
        CustomerOllamaEmbeddingClient customerOllamaEmbeddingClient = new CustomerOllamaEmbeddingClient(ollamaApi);
        customerOllamaEmbeddingClient.withModel("nomic-embed-text:v1.5");
        return customerOllamaEmbeddingClient;
    }
}

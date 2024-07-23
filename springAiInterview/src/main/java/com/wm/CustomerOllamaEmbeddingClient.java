package com.wm;

import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;

import java.util.List;

/**
 * @author: wangm
 * @title: CustomerOllamaEmbeddingClient
 * @projectName: springAiInterview
 * @description:
 * @date: 2024/7/22 9:43
 */

public class CustomerOllamaEmbeddingClient extends OllamaEmbeddingClient {

    public CustomerOllamaEmbeddingClient(OllamaApi ollamaApi) {
        super(ollamaApi);
    }

    @Override
    public List<Double> embed(Document document) {
        String question = (String) document.getMetadata().get("question");
        return this.embed(question);
    }
}

package com.wm.langchain4j._12;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.ollama.*;

public class Main {
    public static void main(String[] args) {
//        OllamaLanguageModel model = OllamaLanguageModel.builder()
//                .baseUrl("http://localhost:11434/v1/")
//                .modelName("qwen2:0.5b")
//                .build();
//        String result = model.generate("你是谁").content();
//        System.out.println(result);

//        OllamaStreamingLanguageModel streamingChatModel = OllamaStreamingLanguageModel.builder()
//                .baseUrl("http://localhost:11434/v1/")
//                .modelName("qwen2:0.5b")
//                .build();
//
//        streamingChatModel.generate("你是谁", new StreamingResponseHandler<String>() {
//            @Override
//            public void onNext(String s) {
//                System.out.println(s);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//        });
        OllamaEmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434/v1/")
                .modelName("nomic-embed-text:v1.5")
                .build();
        System.out.println(embeddingModel.embed("你是谁").content());
    }
}

package com.wm.langchain4j._12;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaLanguageModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

public class Main {
    public static void main(String[] args) {
        OllamaLanguageModel model = OllamaLanguageModel.builder()
                .baseUrl("http://localhost:11434/v1/")
                .modelName("llama3:8b")
                .build();
        String result = model.generate("你是谁").content();
        System.out.println(result);

//        OllamaStreamingChatModel streamingChatModel = OllamaStreamingChatModel.builder()
//                .baseUrl("http://localhost:11434/v1/")
//                .modelName("llama3:8b")
//                .build();
//
//        streamingChatModel.generate("你是谁", new StreamingResponseHandler<AiMessage>() {
//            @Override
//            public void onNext(String s) {
//                System.out.println(s);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println(throwable.fillInStackTrace());
//            }
//        });
    }
}

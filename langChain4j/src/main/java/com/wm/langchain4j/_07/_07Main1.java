package com.wm.langchain4j._07;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;

/**
 * @author: wangm
 * @title: _07Main1
 * @projectName: langChain4j
 * @description:
 * @date: 2024/7/9 17:41
 */
public class _07Main1 {

    interface  AiCustomer{
        TokenStream call(String text);
    }

    public static void main(String[] args) {
        OpenAiEmbeddingModel embeddingModel =  OpenAiEmbeddingModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .build();


        RedisEmbeddingStore embeddingStore = RedisEmbeddingStore.builder()
                .host("127.0.0.1")
                .port(6379)
                .dimension(1536)
                .build();

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(3)
                .minScore(0.8)
                .build();

        StreamingChatLanguageModel model =  ZhipuAiStreamingChatModel
                .builder()
                .apiKey("26f4209ce7fb7ff2fbaaa37ef97cb949.IyYRUNCU9XYeOo03")
                .build();
        // 提示词注入
        ContentInjector contentInjector =  new DefaultContentInjector();


        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentInjector(contentInjector)
                .contentRetriever(contentRetriever)
                .build();

        AiCustomer aiCustomer = AiServices.builder(AiCustomer.class)
                .streamingChatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(2))
                .retrievalAugmentor(retrievalAugmentor)
                .build();

        TokenStream tokenStream = aiCustomer.call("余额什么时候到账");
        tokenStream.onNext(System.out::println)
                .onError(throwable -> {})
                .start();
    }
}

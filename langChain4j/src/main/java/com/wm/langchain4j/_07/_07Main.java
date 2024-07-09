package com.wm.langchain4j._07;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;

import java.util.Map;

/**
 * @author: wangm
 * @title: _07Main
 * @projectName: langChain4j
 * @description:
 * @date: 2024/7/9 17:19
 */
public class _07Main {
    public static void main(String[] args) {
        OpenAiEmbeddingModel embeddingModel =  OpenAiEmbeddingModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("26f4209ce7fb7ff2fbaaa37ef97cb949.IyYRUNCU9XYeOo03")
                .build();


        RedisEmbeddingStore embeddingStore = RedisEmbeddingStore.builder()
                .host("127.0.0.1")
                .port(6379)
                .dimension(1536)
                .build();
        // 内容检索器
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(3)
                .minScore(0.8)
                .build();

        ContentRetriever contentRetriever1 = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(3)
                .minScore(0.8)
                .build();

        ChatLanguageModel model =  OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("26f4209ce7fb7ff2fbaaa37ef97cb949.IyYRUNCU9XYeOo03")
                .build();
        QueryRouter queryRouter = new LanguageModelQueryRouter(model, Map.of(contentRetriever,"商品知识库", contentRetriever1, "订单知识库"));
        queryRouter.route(Query.from("今天新增了那些商品"));
    }
}

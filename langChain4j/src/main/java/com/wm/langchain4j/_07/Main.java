package com.wm.langchain4j._07;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.ExpandingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * @author: wangm
 * @title: Main
 * @projectName: langChain4j
 * @description:
 * @date: 2024/7/9 16:49
 */
public class Main {

    interface  AiCustomer{
        String call(String text);
    }

    public static void main(String[] args) throws URISyntaxException {
        OpenAiEmbeddingModel embeddingModel =  OpenAiEmbeddingModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("26f4209ce7fb7ff2fbaaa37ef97cb949.IyYRUNCU9XYeOo03")
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

        ChatLanguageModel model =  OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("26f4209ce7fb7ff2fbaaa37ef97cb949.IyYRUNCU9XYeOo03")
                .build();
        // 提示词注入
        ContentInjector contentInjector =  new DefaultContentInjector();


        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentInjector(contentInjector)
                .contentRetriever(contentRetriever)
                .build();

        AiCustomer aiCustomer = AiServices.builder(AiCustomer.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(2))
                .retrievalAugmentor(retrievalAugmentor)
                .build();

        Query query = Query.from("余额体现什么时候到账");
        // 将问题转化成3个意思相同的问题
        QueryTransformer queryTransformer = new ExpandingQueryTransformer(model);


        Collection<Query> result = queryTransformer.transform(query);

        //  从向量中查找 向量匹配 q1, q2, q3    1,2,3    1,4,5    1,3,6
        // 内容增强器
        ContentAggregator contentAggregator = new DefaultContentAggregator();

//        QueryRouter queryRouter = new LanguageModelQueryRouter();

    }
}

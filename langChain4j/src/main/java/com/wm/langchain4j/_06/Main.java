package com.wm.langchain4j._06;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;
import okio.FileSystem;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author: wangm
 * @title: Main
 * @projectName: customer
 * @description:
 * @date: 2024/7/3 17:40
 */
public class Main {
    interface  AiCustomer{
        String call(String text);
    }
    public static void main(String[] args) {
        try {
            OpenAiEmbeddingModel embeddingModel =  OpenAiEmbeddingModel.builder()
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .apiKey("26f4209ce7fb7ff2fbaaa37ef97cb949.IyYRUNCU9XYeOo03")
                    .build();


            RedisEmbeddingStore embeddingStore = RedisEmbeddingStore.builder()
                    .host("127.0.0.1")
                    .port(6379)
                    .dimension(1536)
                    .build();

            EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
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

            Path documentPath = Paths.get(Main.class.getClassLoader().getResource("meituan-qa.txt").toURI());
//
//            DocumentParser documentParser = new TextDocumentParser();
//            Document document = FileSystemDocumentLoader.loadDocument(documentPath,documentParser);
//
//            DocumentSplitter documentSplitter = new CustomerServiceDocumentSplitter();
//            List<TextSegment> textSegments = documentSplitter.split(document);
//
//            Response<List<Embedding>> listResponse = embeddingModel.embedAll(textSegments);
//            List<Embedding> embeddingList = listResponse.content();
//
//            embeddingStore.addAll(embeddingList, textSegments);

//            String query = "余额怎么提现";
//            TextSegment textSegment = TextSegment.textSegment(query);
//            Response<Embedding> embed = embeddingModel.embed(textSegment);
//
//            List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(embed.content(), 3, 0.8);
//
//            for (EmbeddingMatch<TextSegment> embeddingMatch : relevant) {
//                System.out.println(embeddingMatch.embedded().text());
//            }

//            List<Content> contents = contentRetriever.retrieve(Query.from("余额怎么提现"));
////
////            System.out.println(contents);
////
////
////            UserMessage userMessage = contentInjector.inject(contents, UserMessage.userMessage("余额怎么提现"));
////
////
////            Response<AiMessage> generate = model.generate(userMessage);
////            System.out.println(generate.content().text());

            RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                    .contentInjector(contentInjector)
                    .contentRetriever(contentRetriever)
                    .build();

            AiCustomer aiCustomer = AiServices.builder(AiCustomer.class)
                    .chatLanguageModel(model)
                    .chatMemory(MessageWindowChatMemory.withMaxMessages(2))
                    .retrievalAugmentor(retrievalAugmentor)
                    .build();

            String result = aiCustomer.call("余额体现什么时候到账");
            System.out.println(result);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

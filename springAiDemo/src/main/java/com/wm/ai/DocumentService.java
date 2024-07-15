package com.wm.ai;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: wangm
 * @title: DocumentService
 * @projectName: springAiDemo
 * @description:
 * @date: 2024/7/15 16:33
 */
@Component
public class DocumentService {

    @Value("classpath:meituan-qa.txt")
    private Resource resource;

    @Autowired
    private RedisVectorStore vectorStore;

    public List<Document> loadDocument(){
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("filename", "meituan-qa.txt");
        List<Document> documents = textReader.get();

        CustomerTextSplitter textSplitter = new CustomerTextSplitter();

        List<Document> list = textSplitter.apply(documents);

        list.forEach(document -> {
            document.getMetadata().put("question", document.getContent().split("\\n")[0]);
        });
        vectorStore.add(list);
        return list;
    }

    public List<Document> search(String message){
        SearchRequest searchRequest = SearchRequest.query(message).withTopK(1);
        return vectorStore.similaritySearch(searchRequest);
    }

    public List<Document> metaDataSearch(String message, String question) {
        return vectorStore.similaritySearch(
                SearchRequest
                        .query(message)
                        .withTopK(5)
                        .withSimilarityThreshold(0.1)
                        .withFilterExpression(String.format("question in ['%s']", question)));
    }
}

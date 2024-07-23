package com.wm;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class InterviewService {

    @Value("classpath:Java基础面试题.md")
    private Resource resource;

    @Autowired
    private RedisVectorStore vectorStore;

    public List<Document> loadText(){
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        // 解析内容
        MarkdownSplitter markdownSplitter = new MarkdownSplitter();
        List<Document> list = markdownSplitter.apply(documents);

        list.forEach(document -> {
            String title = document.getContent().split("==title==")[0];
            String replace = title.replaceAll("##", "");
            document.getMetadata().put("question", replace);
        });
        vectorStore.add(list);

        return list;
    }

    public List<Document> search(String question){
        SearchRequest metaDataSearchRequest = SearchRequest
                .query(question)
                .withTopK(3)
                .withSimilarityThreshold(0.9)
                .withFilterExpression(String.format("question in ['%s']", question));

        List<Document> documents = vectorStore.similaritySearch(metaDataSearchRequest);

        if (!CollectionUtils.isEmpty(documents)) {
            return documents;
        }

        SearchRequest searchRequest = SearchRequest
                .query(question)
                .withTopK(3)
                .withSimilarityThreshold(0.9);

        documents = vectorStore.similaritySearch(searchRequest);

        return documents;
    }
}

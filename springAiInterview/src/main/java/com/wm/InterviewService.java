package com.wm;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    @Value("classpath:Java基础面试题.md")
    private Resource resource;

    @Autowired
    private RedisVectorStore vectorStore;

    private List<Document> loadText(){
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        // 解析内容
        MarkdownSplitter markdownSplitter = new MarkdownSplitter();
        List<Document> apply = markdownSplitter.apply(documents);

    }

    private List<Document> search(String text){

    }
}

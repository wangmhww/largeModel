package com.wm;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wangm
 * @title: DocumentService
 * @projectName: spring-ai-m1-demo
 * @description:
 * @date: 2024/7/23 17:45
 */
@Service
public class DocumentService {

    @Value("classpath:qa.txt")
    private Resource resource;

    @Autowired
    private VectorStore vectorStore;

    public List<Document> loadText(){
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        CustomerTextSplitter textSplitter = new CustomerTextSplitter();
        List<Document> list = textSplitter.apply(documents);

//        vectorStore.add(list);

        return list;
    }
}

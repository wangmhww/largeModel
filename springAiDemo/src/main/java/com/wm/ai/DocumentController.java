package com.wm.ai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: wangm
 * @title: DocumentController
 * @projectName: springAiDemo
 * @description:
 * @date: 2024/7/15 16:40
 */
@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ChatModel chatModel;

    @GetMapping("/document")
    public List<Document> getDocument(){
        return documentService.loadDocument();
    }

    @GetMapping("/documentSearch")
    public List<Document> documentSearch(@RequestParam String message){
        return documentService.search(message);
    }

    @GetMapping("/documentMetaDataSearch")
    public List<Document> documentMetaDataSearch(@RequestParam String message, @RequestParam String question){
        return documentService.metaDataSearch(message, question);
    }

    @GetMapping("/customerSearch")
    public String customerSearch(@RequestParam String question){
        // 向量搜索
        List<Document> documentList = documentService.search(question);

        // 提示词模板
        PromptTemplate promptTemplate = new PromptTemplate("{userMessage}\n\n 用以下信息回答问题\n{contents}");
        // 组装提示词

        Prompt prompt = promptTemplate.create(Map.of("userMessage", question,"contents",documentList));
        // 调用大模型
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }
}

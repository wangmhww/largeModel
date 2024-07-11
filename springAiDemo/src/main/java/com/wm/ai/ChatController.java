package com.wm.ai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: wangm
 * @title: ChatController
 * @projectName: springAiDemo
 * @description:
 * @date: 2024/7/11 17:48
 */
@RestController
public class ChatController {
    @Autowired
    private ChatModel chatModel;



    @GetMapping("/chat")
    public String generate(@RequestParam String message) {
        return chatModel.call(message);
    }

    @Autowired
    private ImageModel imageModel;

    @GetMapping("/image")
    public String image(@RequestParam String message) {
        ImagePrompt imagePrompt = new ImagePrompt(message);
        ImageResponse imageResponse = imageModel.call(imagePrompt);
        return imageResponse.getResult().getOutput().getUrl();
    }

    @Autowired
    private EmbeddingModel embeddingModel;

    @GetMapping("/embedding")
    public List<Double> embedding(@RequestParam String message) {
        return embeddingModel.embed(message);
    }
}

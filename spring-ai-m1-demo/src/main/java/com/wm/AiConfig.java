package com.wm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: wangm
 * @title: AiConfig
 * @projectName: spring-ai-m1-demo
 * @description:
 * @date: 2024/7/23 17:40
 */
@Component
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        ChatClient chatClient = chatClientBuilder.build();
        return chatClient;
    }
}

package com.wm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
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
        ChatClient chatClient = chatClientBuilder
//                .defaultSystem("作一首{style}的诗")
//                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
        return chatClient;
    }
}

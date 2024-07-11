package com.wm.langchain4j._08;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

/**
 * @author: wangm
 * @title: Main
 * @projectName: langChain4j
 * @description:
 * @date: 2024/7/11 15:40
 */
public class Main {
    public static void main(String[] args) {
        ChatLanguageModel model = OpenAiChatModel.builder()
                .baseUrl("http://localhost:5000/v1")
                .apiKey("sk-6RUEARvOb3kRzamr6bD6D9DbCe864a90Bf80Ae10B14d6525")
                .modelName("moonshot-v1-8k")
                .build();

        UserMessage userMessage = UserMessage.from("你是谁");
        Response<AiMessage> response = model.generate(userMessage);
        System.out.println(response.content().text());
    }
}

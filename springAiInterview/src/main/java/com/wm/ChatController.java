package com.wm;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @author: wangm
 * @title: ChatController
 * @projectName: springAiInterview
 * @description:
 * @date: 2024/7/22 9:48
 */
@RestController
public class ChatController {

    @Autowired
    private StreamingChatClient chatClient;

    @Autowired
    private InterviewService interviewService;

    @GetMapping("/document")
    public List<Document> document(){
        return interviewService.loadText();
    }

    @GetMapping("/search")
    public List<Document> searchDocument(@RequestParam("question") String question){
        return interviewService.search(question);
    }

    // 为了兼容前端，实际上应该修改前端代码
    @GetMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OpenAiApi.ChatCompletionChunk> interview(@RequestParam("question") String question) {


        // 向量搜索
        List<Document> documentList = interviewService.search(question);

        // 提示词模板
        PromptTemplate promptTemplate = new PromptTemplate("{userMessage}\n\n 用中文，并根据以下信息回答问题:\n {contents}");

        // 组装提示词
        Prompt prompt = promptTemplate.create(Map.of("userMessage", question, "contents", documentList));

        // 调用大模型
        Flux<ChatResponse> stream = chatClient.stream(prompt);

        return stream.map(chatResponse -> {
            String content = chatResponse.getResult().getOutput().getContent();

            // 需要优化
            OpenAiApi.ChatCompletionChunk chatCompletionChunk = new OpenAiApi.ChatCompletionChunk("1",
                    List.of(new OpenAiApi.ChatCompletionChunk.ChunkChoice(
                            OpenAiApi.ChatCompletionFinishReason.STOP,
                            1,
                            new OpenAiApi.ChatCompletionMessage(
                                    content,
                                    OpenAiApi.ChatCompletionMessage.Role.ASSISTANT)
                            , new OpenAiApi.LogProbs(null))),
                    null, null, null, null);
            return chatCompletionChunk;
        });
    }

}

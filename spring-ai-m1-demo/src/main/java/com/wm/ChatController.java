package com.wm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.model.Content;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * @author: wangm
 * @title: ChatController
 * @projectName: spring-ai-m1-demo
 * @description:
 * @date: 2024/7/23 17:52
 */
@RestController
public class ChatController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;


    @GetMapping("/chat")
    public String chat(){
        return chatClient.prompt().user("你是谁").call().content();
    }

    @GetMapping("/streamClientChat")
    public SseEmitter streamClientChat() {
        SseEmitter sseEmitter = new SseEmitter();
        Flux<String> stream = chatClient.
                prompt().user("你是谁").stream().content();
        stream.subscribe(token->{
            try {
                sseEmitter.send(token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    @GetMapping("/systemClientChat")
    public String systemClientChat(){
        return chatClient.prompt()
                .system(sp-> sp.param("style", "七言绝句"))
                .user("程序员")
                .call()
                .content();
    }

    @GetMapping("/questionAnswerAdvisor")
    public ChatResponse questionAnswerAdvisor(){
        ChatResponse chatResponse = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .user("apikey是什么")
                .call()
                .chatResponse();

        return chatResponse;
    }

    @GetMapping("/chatMemoryChat")
    public String chatMemoryChat(@RequestParam("childId") String childId, @RequestParam("message") String message){

        String content = chatClient.prompt()
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, childId))
                .user(message)
                .call()
                .content();
        return content;
    }

    @GetMapping("/toolChat")
    public String toolChat() {
        Prompt prompt = new Prompt("今天是几月几号", OpenAiChatOptions.builder().withFunction("dateTool").build());
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }

    @GetMapping("/newsToolChat")
    public String newsToolChat() {
        Prompt prompt = new Prompt("今天有那些热点新闻", OpenAiChatOptions.builder().withFunction("playwrightTool").build());
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }

    @GetMapping("/evaluation")
    public EvaluationResponse evaluation() {
        String userText = "apikey是什么";

        ChatResponse response = ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .user(userText)
                .call()
                .chatResponse();

        var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));

        EvaluationRequest evaluationRequest = new EvaluationRequest(userText,
                (List<Content>) response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), response.getResult().getOutput().getContent());

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        return evaluationResponse;
    }

    @GetMapping("/multiModelChat")
    public String multiModelChat() {
        ClassPathResource imageData = new ClassPathResource("/test.jpg");
        UserMessage userMessage = new UserMessage("图片里有什么", List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData)));
        String call = chatModel.call(userMessage);
        return call;
    }
    @GetMapping("/document")
    public List<Document> document(){
        return documentService.loadText();
    }



    @GetMapping("/searchDocument")
    public String searchDocument(String question){
        List<Document> documentList = documentService.searchDocument(question);
        PromptTemplate promptTemplate = new PromptTemplate("{userMessage}\n\n 用以下信息回答问题\n{contents}");

        Prompt prompt = promptTemplate.create(Map.of("userMessage", question, "contents", documentList));
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }
}

package com.wm.ai;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaController {

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        String call = ollamaChatModel.call(message);
        return call;
    }

}

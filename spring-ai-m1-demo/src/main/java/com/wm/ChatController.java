package com.wm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
}

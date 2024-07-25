package com.wm;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangm
 * @title: CustomerTextSplitter
 * @projectName: spring-ai-m1-demo
 * @description:
 * @date: 2024/7/23 17:49
 */
public class CustomerTextSplitter extends TextSplitter {
    @Override
    protected List<String> splitText(String text) {
        return List.of(split(text));
    }

    private String[] split(String text) {
        return text.split("\\s*\\R\\s*\\R\\s*");
    }
}

package com.wm.ai;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

/**
 * @author: wangm
 * @title: CustomerSpliter
 * @projectName: springAiDemo
 * @description:
 * @date: 2024/7/15 16:51
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

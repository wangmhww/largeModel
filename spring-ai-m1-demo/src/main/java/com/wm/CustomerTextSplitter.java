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
        List<String> result = new ArrayList<>();

        return null;
    }
}

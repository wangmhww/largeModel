package com.wm;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

public class MarkdownSplitter extends TextSplitter {

    @Override
    protected List<String> splitText(String text) {
        return List.of();
    }
}

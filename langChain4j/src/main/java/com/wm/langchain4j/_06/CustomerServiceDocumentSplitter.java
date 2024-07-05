package com.wm.langchain4j._06;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.segment.TextSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangm
 * @title: CustomerServiceDocumentSplitter
 * @projectName: customer
 * @description:
 * @date: 2024/7/3 21:42
 */
public class CustomerServiceDocumentSplitter implements DocumentSplitter {
    @Override
    public List<TextSegment> split(Document document) {

        List<TextSegment> segments = new ArrayList<>();

        String[] parts = split(document.text());
        for (String part : parts) {
            segments.add(TextSegment.from(part));
        }

        return segments;
    }

    public String[] split(String text) {
        return text.split("\\s*\\R\\s*\\R\\s*");
    }

}

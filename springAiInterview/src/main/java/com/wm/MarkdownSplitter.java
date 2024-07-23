package com.wm;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.collection.iteration.ReversiblePeekingIterator;
import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.ArrayList;
import java.util.List;

public class MarkdownSplitter extends TextSplitter {

    @Override
    protected List<String> splitText(String text) {
        Parser parser = Parser.builder().build();
        Document markdownDocument = parser.parse(text);

        List<String> result = new ArrayList<>();

        ReversiblePeekingIterator<Node> iterator =  markdownDocument.getChildren().iterator();

        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            // 如果是二级标题
            if (node instanceof Heading && ((Heading) node).getLevel() == 2) {
                if (!builder.isEmpty()) {
                    result.add(builder.toString());
                }
                builder.delete(0, builder.length());
                builder.append(node.getChars());
                builder.append("==title==");
            } else {
                builder.append(node.getChars());
            }
        }
        if (!builder.isEmpty()) {
            result.add(builder.toString());
        }
        return result;
    }
}

package com.wm;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author: wangm
 * @title: PlaywrightTool
 * @projectName: spring-ai-m1-demo
 * @description:
 * @date: 2024/7/24 15:03
 */
@Component
@Description("用来获取今天的热点新闻")
public class PlaywrightTool implements Function<PlaywrightTool.Request, PlaywrightTool.Response> {

    public record Request(String noUse){}

    public record Response(List<String> hotNews){}

    @Override
    public Response apply(Request request) {
        List<String> hostNews = new ArrayList<>();
        try (Playwright playwright = Playwright.create()){
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("https://news.baidu.com/");
            hostNews.addAll(page.locator(".hotnews").allInnerTexts());
        }

        System.out.println(hostNews);
        return new Response(hostNews);
    }
}

package com.wm;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * @author: wangm
 * @title: DateTool
 * @projectName: spring-ai-m1-demo
 * @description:
 * @date: 2024/7/24 14:48
 */
@Component
@Description("用来获取当前时间")
public class DateTool implements Function<DateTool.Request, DateTool.Response> {

    @Override
    public Response apply(Request request) {
        System.out.println(request.noUse);
        return new Response(LocalDateTime.now().toString());
    }

    public record Request(String noUse){}

    public record Response(String date){}


}

package com.wm.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
@Description("获取指定地点的当前时间")
public class DateService implements Function<DateService.Request, DateService.Response> {


    public record Request(@Schema(description = "地点") String address){}

    public record Response(String date){}

    @Override
    public Response apply(Request request) {
        System.out.println(request.address);
        return new Response(String.format("%s的当前时间是%s", request.address, LocalDateTime.now()));
    }

}

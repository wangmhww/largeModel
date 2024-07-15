package com.wm.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

public class DateService2 implements Function<DateService2.Request, DateService2.Response> {


    public record Request(@Schema(description = "地点") String address){}

    public record Response(String date){}

    @Override
    public Response apply(Request request) {
        System.out.println(request.address);
        return new Response(String.format("%s的当前时间是%s", request.address, LocalDateTime.now()));
    }

}

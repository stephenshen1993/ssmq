package com.stephenshen.ssmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * result for MQServer.
 * @author stephenshen
 * @date 2024/7/16 07:06:29
 */
@Data
@AllArgsConstructor
public class Result<T> {
    private int code; // 1==success 0==fail
    private T data;

    public static Result<String> ok() {
        return new Result<>(1, "OK");
    }

    public static Result<String> ok(String msg) {
        return new Result<>(1, msg);
    }

    public static Result<Message<String>> msg(String msg) {
        return new Result<>(1, Message.create(msg, null));
    }

    public static Result<Message<?>> msg(Message<?> msg) {
        return new Result<>(1, msg);
    }

    public static Result<List<Message<?>>> msg(List<Message<?>> msg) {
        return new Result<>(1, msg);
    }
}

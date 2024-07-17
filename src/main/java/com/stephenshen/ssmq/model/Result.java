package com.stephenshen.ssmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;

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

    public static Result<SSMessage<String>> msg(String msg) {
        return new Result<>(1, SSMessage.create(msg, null));
    }

    public static Result<SSMessage<?>> msg(SSMessage<?> msg) {
        return new Result<>(1, msg);
    }
}

package com.stephenshen.ssmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ss message model.
 * @author stephenshen
 * @date 2024/7/13 21:31:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSMessage<T> {

    // private String topic;
    static AtomicLong idgen = new AtomicLong(0);
    private Long id;
    private T body;
    private Map<String, String> headers; // 系统属性， X-version = 1.0
    // private Map<String, String> properties; // 业务属性

    public static long getId(){
        return idgen.getAndIncrement();
    }

    public static SSMessage<String> create(String body, Map<String, String> headers) {
        return new SSMessage<>(getId(), body, headers);
    }
}

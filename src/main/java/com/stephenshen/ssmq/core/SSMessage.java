package com.stephenshen.ssmq.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
    private Long id;
    private T body;
    private Map<String, String> headers; // 系统属性， X-version = 1.0
    // private Map<String, String> properties; // 业务属性
}

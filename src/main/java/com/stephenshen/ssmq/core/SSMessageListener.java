package com.stephenshen.ssmq.core;

/**
 * message listener.
 * @author stephenshen
 * @date 2024/7/14 15:40:36
 */
public interface SSMessageListener<T> {

    void onMessage(SSMessage<T> message);
}

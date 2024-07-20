package com.stephenshen.ssmq.client;

import com.stephenshen.ssmq.model.Message;

/**
 * message listener.
 * @author stephenshen
 * @date 2024/7/14 15:40:36
 */
public interface SSListener<T> {

    void onMessage(Message<T> message);
}

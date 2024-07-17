package com.stephenshen.ssmq.client;

import com.stephenshen.ssmq.model.SSMessage;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * mq for topic.
 * @author stephenshen
 * @date 2024/7/13 21:40:03
 */
public class SSMq {

    private String topic;
    private LinkedBlockingQueue<SSMessage> queue = new LinkedBlockingQueue<>();
    private List<SSListener> listeners = new ArrayList<>();

    public SSMq(String topic) {
        this.topic = topic;
    }

    public boolean send(SSMessage message) {
        boolean offer = queue.offer(message);
        listeners.forEach(listener -> listener.onMessage(message));
        return offer;
    }

    // 拉模式获取消息
    @SneakyThrows
    public <T> SSMessage<T> poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    public <T> void listen(SSListener<T> listener) {
        listeners.add(listener);
    }
}

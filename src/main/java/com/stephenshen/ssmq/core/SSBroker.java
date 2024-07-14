package com.stephenshen.ssmq.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * broker for topics.
 * @author stephenshen
 * @date 2024/7/13 21:40:47
 */
public class SSBroker {

    Map<String, SSMq> mqMapping = new ConcurrentHashMap<>(64);
    public SSMq find(String topic) {
        return mqMapping.get(topic);
    }

    public SSMq createTopic(String topic) {
        return mqMapping.putIfAbsent(topic, new SSMq(topic));
    }

    public SSProducer createProducer() {
        return new SSProducer(this);
    }

    public SSConsumer<?> createPConsumer(String topic) {
        SSConsumer<?> consumer = new SSConsumer<>(this);
        consumer.subscribe(topic);
        return consumer;
    }
}

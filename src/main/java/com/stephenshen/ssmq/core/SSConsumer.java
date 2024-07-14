package com.stephenshen.ssmq.core;

/**
 * message consumer.
 * @author stephenshen
 * @date 2024/7/13 21:47:50
 */
public class SSConsumer<T> {

    SSBroker broker;
    String topic;
    SSMq mq;

    public SSConsumer(SSBroker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {
        this.topic = topic;
        this.mq = broker.find(topic);
        if (mq == null) throw new RuntimeException("topic not found");
    }

    public SSMessage<T> poll(long timeout) {
        return mq.poll(timeout);
    }

    public void listen(SSMessageListener<T> listener) {
        mq.listen(listener);
    }
}

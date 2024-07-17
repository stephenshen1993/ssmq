package com.stephenshen.ssmq.client;

import com.stephenshen.ssmq.model.SSMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * message consumer.
 * @author stephenshen
 * @date 2024/7/13 21:47:50
 */
public class SSConsumer<T> {

    private String id;
    SSBroker broker;
    String topic;
    SSMq mq;

    static AtomicInteger idgen = new AtomicInteger(0);

    public SSConsumer(SSBroker broker) {
        this.broker = broker;
        this.id = "CID" + idgen.getAndDecrement();
    }

    public void subscribe(String topic) {
        this.topic = topic;
        this.mq = broker.find(topic);
        if (mq == null) throw new RuntimeException("topic not found");
    }

    public SSMessage<T> poll(long timeout) {
        return mq.poll(timeout);
    }

    public void listen(SSListener<T> listener) {
        mq.listen(listener);
    }
}

package com.stephenshen.ssmq.client;

import com.stephenshen.ssmq.model.Message;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * message consumer.
 * @author stephenshen
 * @date 2024/7/13 21:47:50
 */
public class SSConsumer<T> {

    private String id;
    SSBroker broker;
    SSMq mq;

    static AtomicInteger idgen = new AtomicInteger(0);

    public SSConsumer(SSBroker broker) {
        this.broker = broker;
        this.id = "CID" + idgen.getAndDecrement();
    }

    public void sub(String topic) {
        broker.sub(topic, id);
    }

    public void unsub(String topic) {
        broker.unsub(topic, id);
    }

    public Message<T> recv(String topic) {
        return broker.recv(topic, id);
    }

    public boolean ack(String topic, int offset) {
        return broker.ack(topic, id, offset);
    }

    public boolean ack(String topic, Message<?> message) {
        int offset = Integer.parseInt(message.getHeaders().get("X-offset"));
        return ack(topic, offset);
    }

    // TODO
    public void listen(String topic, SSListener<T> listener) {
        listener = listener;
        broker.addConsumer(topic, this);
    }

    @Getter
    private SSListener listener;
}

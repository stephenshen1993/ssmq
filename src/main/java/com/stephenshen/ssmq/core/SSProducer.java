package com.stephenshen.ssmq.core;

import lombok.AllArgsConstructor;

/**
 * @author stephenshen
 * @date 2024/7/13 21:37:33
 */
@AllArgsConstructor
public class SSProducer {

    SSBroker broker;

    public boolean send(String topic, SSMessage message) {
        SSMq mq = broker.find(topic);
        if (mq == null) throw new RuntimeException("topic not found");
        return mq.send(message);
    }
}

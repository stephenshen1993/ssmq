package com.stephenshen.ssmq.client;

import com.stephenshen.ssmq.model.Message;
import lombok.AllArgsConstructor;

/**
 * @author stephenshen
 * @date 2024/7/13 21:37:33
 */
@AllArgsConstructor
public class SSProducer {

    SSBroker broker;

    public boolean send(String topic, Message message) {
        return broker.send(topic, message);
    }
}
